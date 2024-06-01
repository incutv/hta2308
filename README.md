
<div align=center>
	<h3>📚 "HTA 멘토링을 위한 코드입니다." 📚</h3>
</div>
<br><br>

# 📖 실습 환경
* spring boot 3.x
* java 17
* caffine cache
* h2
* Maven
* intellij

## 첫번째 미션
### 목적:캐싱활용 캐싱을 통해 성능 개선 경험
* 카페인 캐시 적용 예시 정리 노션 페이지 : https://witty-toad-850.notion.site/spring-boot-3-Local-cache-caffeine-1-11acd00020a84571a14ebf544b53f796?pvs=4
* notice 테이블에는 조회수 필드가 있습니다.
* 조회수를 기반으로 HOT 게시글 10개를 출력하고자 합니다. ( 커뮤니티에 핫글이라고 생각해주시면 됩니다.)
  * HOT 게시글 10개애 대한 데이터를 캐싱 처리하고자 합니다.( 카페인 캐시 활용)
    * 조회수가 동일한경우에는 최근에 생성한 공지사항 게시글순으로 나올수있게 개선해주세요. - (단, java 문법을 활용하여 정렬 처리 해주세요.)

* spring boot gmail 발송 기능을 추가해주세요.
  * from(noreplay 고정) , to(본인 이메일) , message( notice 테이블에 content를)
  * [new] 이메일 발송 java completedfuture 를 활용하여 비동기 발송으로 처리할 수 있게 개선해주세요.
    * [new] 비동기 방식으로 발송을 하다가 이메일 발송 실패시 인지를 잘 할 수 있게 예외처리도 신경써서 해주세요.

## 두번째 미션
### 목적: @scheduled활용 실무에 서버이중화환경에서 발생할수 있는 이슈 개선
* spring boot scheduled를 활용하여 매일 18:00에 HOT 게시글 10개를 본인 이메일에 발송되게 스케줄링 설정
  *  [new] 실무의 경우 WAS( 저희의 경우 tomcat)가 최소 2대 이상으로 이중화 되어 있습니다.
  *  [new] @scheduled를 사용시 서버가 이중화가 되어 있어 동작이 동시에 여러번 발생될 수 있게 이게 문제로 이어질 수 있는데요. 아래 블로그를 참고하여 개선해보기
  *  [new] https://blog.naver.com/hohomax/223380841680


         






# HTApractice
# hta2308
