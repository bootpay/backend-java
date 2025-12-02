#!/bin/bash
set -e

echo "🚀 Central Portal 배포 시작..."
echo "========================================"

# 프로젝트 루트로 이동
cd /Users/taesupyoon/bootpay/server/sdk/java

echo "📦 Step 1: 기존 빌드 정리..."
rm -rf core/build/repo
rm -f central-bundle.zip

echo "📦 Step 2: 새로운 publication 생성..."
./gradlew core:publishReleasePublicationToLocalRepoRepository

echo "📦 Step 3: 번들 생성..."
cd core/build/repo
zip -r ../../../central-bundle.zip \
  ${PUBLISH_GROUP_ID//.//}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/*.aar \
  ${PUBLISH_GROUP_ID//.//}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/*.pom \
  ${PUBLISH_GROUP_ID//.//}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/*.module \
  ${PUBLISH_GROUP_ID//.//}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/*.md5 \
  ${PUBLISH_GROUP_ID//.//}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/*.sha1 \
  ${PUBLISH_GROUP_ID//.//}/${PUBLISH_ARTIFACT_ID}/${PUBLISH_VERSION}/*.asc
cd ../../../

echo "✅ 번들 생성 완료: $(ls -lh central-bundle.zip)"

echo "🔐 Step 4: 인증 정보 설정..."
OSSRH_USERNAME="i4oDa5"
OSSRH_PASSWORD="uh9Wgv6DYCHET2H8M2XLDIKnP82Eigtdz"
BEARER_TOKEN=$(echo -n "${OSSRH_USERNAME}:${OSSRH_PASSWORD}" | base64)

echo "⬆️  Step 5: Central Portal에 업로드..."
DEPLOYMENT_ID=$(curl --silent --request POST \
  --header "Authorization: Bearer ${BEARER_TOKEN}" \
  --form bundle=@central-bundle.zip \
  https://central.sonatype.com/api/v1/publisher/upload)

if [ -z "$DEPLOYMENT_ID" ]; then
    echo "❌ 업로드 실패!"
    exit 1
fi

echo "✅ 업로드 성공!"
echo "📋 Deployment ID: $DEPLOYMENT_ID"

echo "⏳ Step 6: 배포 상태 확인 중..."
sleep 5

echo "📊 Step 7: 상태 조회..."
STATUS_RESPONSE=$(curl --silent --request POST \
  --header "Authorization: Bearer ${BEARER_TOKEN}" \
  "https://central.sonatype.com/api/v1/publisher/status?id=${DEPLOYMENT_ID}")

echo "📄 배포 상태:"
echo "$STATUS_RESPONSE" | jq .

# 상태 확인
DEPLOYMENT_STATE=$(echo "$STATUS_RESPONSE" | jq -r '.deploymentState')
echo ""
echo "========================================"
echo "🎯 현재 상태: $DEPLOYMENT_STATE"

case $DEPLOYMENT_STATE in
    "PENDING")
        echo "⏳ 검증 대기 중입니다."
        ;;
    "VALIDATING")
        echo "🔍 검증 진행 중입니다."
        ;;
    "VALIDATED")
        echo "✅ 검증 완료! 수동 배포가 필요합니다."
        echo "🚀 자동 배포를 시도합니다..."
        
        PUBLISH_RESPONSE=$(curl --silent --request POST \
          --header "Authorization: Bearer ${BEARER_TOKEN}" \
          --write-out "HTTPSTATUS:%{http_code}" \
          "https://central.sonatype.com/api/v1/publisher/deployment/${DEPLOYMENT_ID}")
        
        HTTP_STATUS=$(echo $PUBLISH_RESPONSE | grep -o "HTTPSTATUS:[0-9]*" | cut -d: -f2)
        
        if [ "$HTTP_STATUS" -eq "204" ]; then
            echo "🎉 배포 시작됨! Maven Central에 곧 반영됩니다."
        else
            echo "⚠️  수동 배포 실패. Central Portal에서 수동으로 배포하세요."
        fi
        ;;
    "PUBLISHING")
        echo "🚀 Maven Central에 배포 중입니다."
        ;;
    "PUBLISHED")
        echo "🎉 배포 완료! Maven Central에서 사용 가능합니다."
        ;;
    "FAILED")
        echo "❌ 배포 실패!"
        echo "🔍 오류 내용:"
        echo "$STATUS_RESPONSE" | jq '.errors'
        ;;
    *)
        echo "❓ 알 수 없는 상태: $DEPLOYMENT_STATE"
        ;;
esac

echo ""
echo "========================================"
echo "🌐 Central Portal 확인: https://central.sonatype.com/"
echo "📋 Deployment ID: $DEPLOYMENT_ID"
echo "🏁 스크립트 완료!" 