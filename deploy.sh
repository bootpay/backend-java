#!/bin/bash
set -euo pipefail

# Maven Central (Central Portal) 배포 스크립트
#
# ⚠️ 자격증명은 이 파일에 넣지 않는다. gitignored 인 local.properties 에서 읽는다.
#    (2026-08-24: 이 스크립트에 ossrhUsername/ossrhPassword 가 하드코딩된 채
#     public 리포에 커밋돼 있었다. 값은 폐기·재발급 대상이다.)
# ⚠️ 버전은 publish.gradle 의 PUBLISH_VERSION 에서 읽는다 (수동 동기화 금지).

cd "$(dirname "$0")"

read_prop() {
  # local.properties 에서 key 값을 읽는다 (없으면 빈 문자열)
  awk -F= -v k="$1" '$1==k {sub(/^[^=]*=/,""); print; exit}' local.properties 2>/dev/null | tr -d '\r'
}

read_gradle_ext() {
  # publish.gradle 의 ext 값을 읽는다
  awk -F"'" -v k="$1" '$0 ~ k"[[:space:]]*=" {print $2; exit}' publish.gradle
}

PUBLISH_GROUP_ID=$(read_gradle_ext PUBLISH_GROUP_ID)
PUBLISH_ARTIFACT_ID=$(read_gradle_ext PUBLISH_ARTIFACT_ID)
PUBLISH_VERSION=$(read_gradle_ext PUBLISH_VERSION)
GROUP_PATH="${PUBLISH_GROUP_ID//.//}"

OSSRH_USERNAME=$(read_prop ossrhUsername)
OSSRH_PASSWORD=$(read_prop ossrhPassword)

if [ -z "$OSSRH_USERNAME" ] || [ -z "$OSSRH_PASSWORD" ]; then
  echo "❌ local.properties 에 ossrhUsername / ossrhPassword 가 없습니다."
  exit 1
fi

echo "🚀 Central Portal 배포: ${PUBLISH_GROUP_ID}:${PUBLISH_ARTIFACT_ID}:${PUBLISH_VERSION}"
echo "========================================"

# 이미 배포된 버전이면 중단 (Maven Central 은 덮어쓸 수 없다)
POM_URL="https://repo1.maven.org/maven2/${GROUP_PATH}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/${PUBLISH_ARTIFACT_ID}-${PUBLISH_VERSION}.pom"
if [ "$(curl -s -o /dev/null -w '%{http_code}' "$POM_URL")" = "200" ]; then
  echo "❌ ${PUBLISH_VERSION} 은 이미 Maven Central 에 있습니다. 버전을 올리세요."
  exit 1
fi

echo "📦 Step 1: 테스트"
./gradlew :core:test --rerun-tasks

echo "📦 Step 2: 기존 빌드 정리"
rm -rf core/build/repo
rm -f central-bundle.zip

echo "📦 Step 3: publication 생성"
./gradlew core:publishReleasePublicationToLocalRepoRepository

echo "📦 Step 4: 번들 생성"
(cd core/build/repo && zip -r ../../../central-bundle.zip "${GROUP_PATH}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/")
echo "✅ $(ls -lh central-bundle.zip | awk '{print $9, $5}')"

BEARER_TOKEN=$(printf '%s:%s' "${OSSRH_USERNAME}" "${OSSRH_PASSWORD}" | base64)

echo "⬆️  Step 5: Central Portal 업로드"
DEPLOYMENT_ID=$(curl --silent --fail --request POST \
  --header "Authorization: Bearer ${BEARER_TOKEN}" \
  --form bundle=@central-bundle.zip \
  https://central.sonatype.com/api/v1/publisher/upload)

if [ -z "$DEPLOYMENT_ID" ]; then
  echo "❌ 업로드 실패"
  exit 1
fi
echo "✅ Deployment ID: $DEPLOYMENT_ID"

echo "⏳ Step 6: 상태 폴링 (최대 5분)"
for _ in $(seq 1 30); do
  STATUS_RESPONSE=$(curl --silent --request POST \
    --header "Authorization: Bearer ${BEARER_TOKEN}" \
    "https://central.sonatype.com/api/v1/publisher/status?id=${DEPLOYMENT_ID}")
  STATE=$(echo "$STATUS_RESPONSE" | jq -r '.deploymentState')
  echo "   상태: $STATE"

  case "$STATE" in
    VALIDATED)
      echo "🚀 검증 완료 — 배포를 시작합니다."
      HTTP_STATUS=$(curl --silent --output /dev/null --write-out '%{http_code}' --request POST \
        --header "Authorization: Bearer ${BEARER_TOKEN}" \
        "https://central.sonatype.com/api/v1/publisher/deployment/${DEPLOYMENT_ID}")
      [ "$HTTP_STATUS" = "204" ] && echo "🎉 배포 시작됨" || echo "⚠️ 자동 배포 실패 — Central Portal 에서 수동 배포하세요 (HTTP $HTTP_STATUS)"
      ;;
    PUBLISHED)
      echo "🎉 배포 완료 — Maven Central 반영까지 수 분 걸릴 수 있습니다."
      exit 0
      ;;
    FAILED)
      echo "❌ 배포 실패:"; echo "$STATUS_RESPONSE" | jq '.errors'
      exit 1
      ;;
  esac
  python3 -c 'import time; time.sleep(10)'
done

echo "⏱️  5분 내에 PUBLISHED 로 끝나지 않았습니다. Central Portal 에서 확인하세요."
echo "🌐 https://central.sonatype.com/  ·  Deployment ID: $DEPLOYMENT_ID"
