
## PG Analytics - 결제데이터 분석서비스
* 기존 PG 사를 이용 중이신 사업자도 별도의 계약없이 부트페이를 통해 결제 연동과 통계를 무료로 이용하실 수 있습니다.
* 한줄의 소스코드로 인사이트를 얻어 매출을 극대화하세요.



## 결제 검증 및 취소 - 서버사이드용
* 보안상의 이유로 결제검증과 취소는 서버사이드에서 이루어집니다.
* 부트페이 서버와 통신시 Rest용 Application Id, Private Key 값을 보내주셔야 하며, 보내실 서버의 IP는 미리 등록하셔야 합니다.

## 설치  

build.gradle (project)
```
pip install bootpay 
```

## 샘플 코드
```python 
from bootpay import Bootpay

bootpay = Bootpay('5b8f6a4d396fa665fdc2b5ea', 'rm6EYECr6aroQVG2ntW0A6LpWnkTgP4uQ3H18sDDUYw=')
result = bootpay.get_access_token() # 토큰 얻어오기

receipt_id = '612e09260d681b0021e61ab9' # 검증할 결제 영수증 ID
print(bootpay.verify(receipt_id)) # 결제검증하기
```

## Documentation

[부트페이 개발매뉴얼](https://app.gitbook.com/@bootpay)을 참조해주세요

## 기술문의

[부트페이 홈페이지](https://www.bootpay.co.kr) 우측 하단 채팅을 통해 기술문의 주세요!

## License

The gem is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).

