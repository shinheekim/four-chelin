# Index, Cache, 동시성 제어를 활용한 성능 개선 프로젝트

![캐치테이블.jpg](img%2F%EC%BA%90%EC%B9%98%ED%85%8C%EC%9D%B4%EB%B8%94.jpg)

## 프로젝트 목표

#### 👊 CatchTable 프로젝트는 인덱스 최적화, 캐시 활용, 그리고 동시성 제어 기법을 통해 시스템 성능을 대폭 개선하는 것을 목표로 합니다. 👊

### 프로젝트 진행 기간: 24.11.22 ~ 24.11.29

## 👨‍👨‍👧‍👧 팀 구성

| 이름    | 역할 | 담당 기능                                                       |
|-------|----|-------------------------------------------------------------|
| 박가온누리 | 팀원 | 복합 캐시(로컬, 글로벌)를 활용한 세션 기반 회원가입, 로그인, 회원정보수정, 회원탈퇴           |
| 백한비   | 팀원 | 로컬 캐시를 활용한 인기검색어, 키워드를 통한 가게 조회 기능 성능 개선                    |
| 김신희   | 팀원 | 가게 웨이팅 시 동시성 제어 기법을 활용하여 데이터 정합성 개선                         |
| 박인선   | 팀원 | 대용량 csv 데이터를 데이터베이스에 Insert, 필터를 통한 가게 조회 시 인덱스를 사용한 검색 최적화 |

## Tools

### 🖥 language & Server 🖥

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <br>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/redis-%23FF4438?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/QueryDSL-%2339729E?style=for-the-badge&logo=QueryDSL&logoColor=white"> <hr>

### 👏 Cowork Tools 👏

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <br> 
<img src="https://img.shields.io/badge/notion-000000?style=or-the-badge&logo=notion&logoColor=white"/> <img src="https://img.shields.io/badge/Slack-FE5196?style=or-the-badge&logo=slack&logoColor=white"/>
<br>
<hr/>

## 와이어 프레임

### 회원가입/로그인

![member.jpeg](img%2Fmember.jpeg)

### 인기검색어 및 검색어를 통한 가게조회

![search.jpeg](img%2Fsearch.jpeg)

### 업종, 미슐랭 점수를 필터로 사용한 가게조회

![store.jpeg](img%2Fstore.jpeg)

## 회원 관리 API 명세

<table>
    <tr>
        <th>API&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
        <th>Method</th>
        <th>EndPoint</th>
        <th>Request</th>
        <th>Request Type</th>
        <th>Response</th>
        <th>Response Type</th>
        <th>Status</th>
    </tr>
    <tr>
        <td>회원가입</td>
        <td>POST</td>
        <td><code>/api/members/signup</code></td>
        <td><pre lang="json">{
    "phone": "01011112222",
    "nickname": "honggildong",
    "password": "hong1234",
    "role": "USER"
}</pre></td>
        <td><code>application/json</code></td>
        <td><pre lang="json">{
    "statusCode": 201,
    "message": "회원가입에 성공하였습니다.",
    "data": {
        "id": 1,
        "phone": "01011112222",
        "nickname": "honggildong"
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>201</td>
    </tr>
     <tr>
        <td>로그인</td>
        <td>POST</td>
        <td><code>/api/members/login</code></td>
        <td><pre lang="json">{
    "phone": "01011112222",
    "password": "hong1234"
}</pre></td>
        <td><code>application/json</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "로그인에 성공하였습니다.",
    "data": {
        "id": 1,
        "phone": "01011112222",
        "nickname": "honggildong",
        "role": "USER"
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>회원 정보 조회(DB)</td>
        <td>GET</td>
        <td><code>/api/members/profile</code></td>
        <td><code>N/A</code></td>
        <td><code>N/A</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "로그인한 회원정보를 불러왔습니다.",
    "data": {
        "id": 1,
        "phone": "01011112222",
        "nickname": "honggildong",
        "role": "USER"
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>회원 정보 조회(LocalCache)</td>
        <td>GET</td>
        <td><code>/api/members/v2</code></td>
        <td><code>N/A</code></td>
        <td><code>N/A</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "로그인한 회원정보를 불러왔습니다.",
    "data": {
        "id": 1,
        "phone": "01011112222",
        "nickname": "honggildong",
        "role": "USER"
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>회원 정보 수정</td>
        <td>PUT</td>
        <td><code>/api/members</code></td>
        <td><pre lang="json">{
    "nickname": "honggildong수정",
    "password": "hong1111"
}</pre></td>
        <td><code>application/json</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "회원정보 수정에 성공하였습니다.",
    "data": {
        "id": 1,
        "phone": "01011112222",
        "nickname": "honggildong수정"",
        "role": "USER"
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>회원 탈퇴</td>
        <td>DELETE</td>
        <td><code>/api/members</code></td>
        <td><pre lang="json">{
    "password": "hong1111"
}</pre></td>
        <td><code>application/json</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "회원정보 삭제에 성공하였습니다.",
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
</table>

## 필터를 통한 가게 조회 API

<table>
    <tr>
        <th>API&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
        <th>Method</th>
        <th>EndPoint</th>
        <th>Request</th>
        <th>Request Type</th>
        <th>Response</th>
        <th>Response Type</th>
        <th>Status</th>
    </tr>
    <tr>
        <td>업종, 별점별 가게조회</td>
        <td>GET</td>
        <td><code>/api/stores</code></td>
        <td><code>N/A</code></td>
        <td><code>RequestParm</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "가게가 조회되었습니다",
    "data": {
        "storeResponses": [
            {
                "id": 132,
                "storeName": "팔분갈비찜",
                "status": "OPEN",
                "category": "한식",
                "address": "서울특별시 성동구 동일로55길 3, 1층 (송정동)",
                "star": 3
            },
            {
                "id": 28743,
                "storeName": "단수이 대왕 카스테라",
                "status": "OPEN",
                "category": "한식",
                "address": "서울특별시 노원구 동일로180길 59, 1층 104호 (공릉동)",
                "star": 3
            },
            {
                "id": 90331,
                "storeName": "엄나무",
                "status": "OPEN",
                "category": "한식",
                "address": "서울특별시 동대문구 전농로29길 107 (전농동)",
                "star": 3
            },
            {
                "id": 53342,
                "storeName": "케이집밥",
                "status": "OPEN",
                "category": "한식",
                "address": "서울특별시 노원구 동일로204가길 34, 씨앤미복합빌딩 지하1층 B146호 (중계동)",
                "star": 3
            },
            {
                "id": 90404,
                "storeName": "국물닭발과 양꾸이",
                "status": "OPEN",
                "category": "기타",
                "address": "서울특별시 성동구 무수막길 91-1, 1층 (금호동2가)",
                "star": 3
            },
            {
                "id": 90359,
                "storeName": "교촌치킨 연희점",
                "status": "OPEN",
                "category": "치킨",
                "address": "서울특별시 서대문구 연희로 81-30, 1층 (연희동)",
                "star": 3
            },
            {
                "id": 55617,
                "storeName": "주식회사 키토산 우이산장",
                "status": "OPEN",
                "category": "한식",
                "address": "서울특별시 강북구 삼양로181길 141-11 (우이동)",
                "star": 3
            },
            {
                "id": 90361,
                "storeName": "김사부아구찜 신촌점",
                "status": "OPEN",
                "category": "한식",
                "address": "서울특별시 마포구 서강로9길 17, 104동 지층 비101호 주방7호 (창전동, 신촌금호아파트)",
                "star": 3
            },
            {
                "id": 90409,
                "storeName": "수퍼(SOUPER)",
                "status": "OPEN",
                "category": "기타",
                "address": "서울특별시 송파구 올림픽로 240, 롯데백화점 잠실점 지하1층 (잠실동)",
                "star": 3
            },
            {
                "id": 70061,
                "storeName": "산솔 신논현점",
                "status": "OPEN",
                "category": "일식",
                "address": "서울특별시 강남구 강남대로112길 15, 지상1층 (논현동)",
                "star": 3
            }
        ],
        "totalPages": 2985,
        "totalElements": 29844
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
     <tr>
        <td>CSV 등록</td>
        <td>POST</td>
        <td><code>/api/stores/collection</code></td>
        <td><code>N/A</code></td>
        <td><code>form-data</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "csv 파일 데이터가 성공적으로 데이터베이스에 입력되었습니다"
}</pre></td>
        <td><code>application/json</code></td>
        <td>201</td>
    </tr>
</table>

## 웨이팅 관리 API

<table>
    <tr>
        <th>API&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
        <th>Method</th>
        <th>EndPoint</th>
        <th>Request</th>
        <th>Request Type</th>
        <th>Response</th>
        <th>Response Type</th>
        <th>Status</th>
    </tr>
    <tr>
        <td>웨이팅 등록</td>
        <td>POST</td>
        <td><code>/api/waitings</code></td>
        <td><code>{
	"waitingType": "ON_SITE",
	"mealType": "TAKEOUT", 
	"personnel": 3, 
	"storeId": 1
}</code></td>
        <td><code>application/json</code></td>
        <td><pre lang="json">{
	"statusCode": 201,
	"message": "성공적으로 웨이팅 신청되었습니다.",
	"data" : 
		{
			"storeName": "유즈라멘",
			"mealType": "TAKEOUT",
			"personnel": 3,
			"waitingType": "ON_SITE",
			"waitingNum": 58 // 웨이팅 번호,
			"waitingStatus": "WAITING"
		}
	}</pre></td>
        <td><code>application/json</code></td>
        <td>201</td>
    </tr>
     <tr>
        <td>웨이팅 조회</td>
        <td>GET</td>
        <td><code>/api/waitings</code></td>
        <td><code>N/A</code></td>
        <td><code>RequestParam</code></td>
        <td><pre lang="json">{
	"statusCode": 200,
	"message": "성공적으로 웨이팅 조회되었습니다.",
	"data" : 
		{
			{
				"information": 
					{
						"storeName": "유즈라멘",
						"mealType": "TAKEOUT",
						"personnel": 3,
						"waitingType": "ON_SITE",
						"waitingNum": 58 // 웨이팅 번호,
						"waitingStatus": "WAITING"
					}
				"storeCategory": "일식", 
				"storeLocation": "서울역",
				"createAt": "2024-11-25"
			}
		}
	}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>웨이팅 삭제</td>
        <td>DELETE</td>
        <td><code>/api/waitings</code></td>
        <td><code>N/A</code></td>
        <td><code>PathVariable</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": "성공적으로 웨이팅을 취소하였습니다.",
    "data": null
}</pre></td>
        <td><code>application/json</code></td>
        <td>204</td>
    </tr>
</table>

## 인기검색어, 검색어를 통한 가게 조회 API

<table>
    <tr>
        <th>API&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
        <th>Method</th>
        <th>EndPoint</th>
        <th>Request</th>
        <th>Request Type</th>
        <th>Response</th>
        <th>Response Type</th>
        <th>Status</th>
    </tr>
    <tr>
        <td>검색기록, 인기검색어 조회(DB)</td>
        <td>GET</td>
        <td><code>/api/searches/v1</code></td>
        <td><code>N/A</code></td>
        <td><code>N/A</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": null,
    "data": {
        "userSearchHistory": [
            "카페",
            "짜장면",
            "햄버거",
            "스테이크"
        ],
        "popularKeywords": [
            "카페",
            "햄버거",
            "피자",
            "치킨",
            "족발"
        ]
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
     <tr>
        <td>검색기록, 인기검색어 조회(LocalCache)</td>
        <td>GET</td>
        <td><code>/api/searches/v2</code></td>
        <td><code>N/A</code></td>
        <td><code>N/A</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": null,
    "data": {
        "userSearchHistory": [
            "카페",
            "짜장면",
            "햄버거",
            "스테이크"
        ],
        "popularKeywords": [
            "카페",
            "햄버거",
            "피자",
            "치킨",
            "족발"
        ]
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>검색어를 통한 가게 조회(DB)</td>
        <td>GET</td>
        <td><code>/api/searches/v1/stores</code></td>
        <td><code>N/A</code></td>
        <td><code>N/A</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": null,
    "data": {
        "storeResponses": [
            {
                "id": 1,
                "storeName": "아카페라",
                "status": "OPEN",
                "category": "카페",
                "address": "서울시 강남구",
                "star": 2
            },
            {
                "id": 2,
                "storeName": "카페 라떼는말이야",
                "status": "OPEN",
                "category": "카페",
                "address": "서울시 종로구",
                "star": 3
            }
        ],
        "totalPages": 1,
        "totalElements": 2
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
    <tr>
        <td>검색어를 통한 가게 조회(LocalCache)</td>
        <td>GET</td>
        <td><code>/api/searches/v2/stores</code></td>
        <td><code>N/A</code></td>
        <td><code>N/A</code></td>
        <td><pre lang="json">{
    "statusCode": 200,
    "message": null,
    "data": {
        "storeResponses": [
            {
                "id": 1,
                "storeName": "아카페라",
                "status": "OPEN",
                "category": "카페",
                "address": "서울시 강남구",
                "star": 2
            },
            {
                "id": 2,
                "storeName": "카페 라떼는말이야",
                "status": "OPEN",
                "category": "카페",
                "address": "서울시 종로구",
                "star": 3
            }
        ],
        "totalPages": 1,
        "totalElements": 2
    }
}</pre></td>
        <td><code>application/json</code></td>
        <td>200</td>
    </tr>
</table>

## ERD

![fourchelin_ERD.jpeg](img%2Ffourchelin_ERD.jpeg)

## 프로젝트 구조

```plaintext
├─common
│  ├─baseentity
│  ├─config
│  ├─exception
│  ├─filter
│  ├─security
│  ├─service
│  └─template
└─domain
    ├─member
    │  ├─controller
    │  ├─dto
    │  ├─entity
    │  ├─enums
    │  ├─exception
    │  ├─repository
    │  └─service
    ├─search
    │  ├─controller
    │  ├─dto
    │  ├─entity
    │  ├─exception
    │  ├─repository
    │  └─service
    ├─store
    │  ├─controller
    │  ├─dto
    │  ├─entity
    │  ├─enums
    │  ├─exception
    │  ├─repository
    │  └─service
    └─waiting
       ├─controller
       ├─dto
       ├─entity
       ├─enums
       ├─exception
       ├─repository
       └─service
```




