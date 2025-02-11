/**
 * 
 */
document.addEventListener("DOMContentLoaded", function () {
      	const token = localStorage.getItem("token");
          const loginBtn = document.getElementById("login");
		  const registerBtn = document.getElementById("register");
          const logoutBtn = document.getElementById("logout");
          const myPageBtn = document.getElementById("mypage");

          if (!token) {
              //alert("🚨 로그인이 필요합니다.");
              loginBtn.style.display = "inline-block"; // 로그인 버튼 보이기
			  registerBtn.style.display="inline-block";
			  logoutBtn.style.display = "none"; // 로그아웃 숨기기
              myPageBtn.style.display = "none"; // 마이페이지 숨기기
              return;
          }
		  
		  //로그아웃 처리
		  if (logoutBtn) {
		          logoutBtn.addEventListener("click", function (event) {
		              event.preventDefault();
		              localStorage.removeItem("token"); // JWT 삭제
		              sessionStorage.removeItem("token"); 
		              window.location.href = "/user/login"; // 로그인 페이지로 이동
		          });
		      }
			  // 마이페이지 사용자 정보 가져오기
			  function getUserInfo() {
			      const token = localStorage.getItem("token");
			      if (!token) {
					alert("로그인이 필요")
					return;
				  }
			      fetch("/api/mypage", {
			          method: "GET",
			          headers: {
			              "Authorization": "Bearer " + token
			          }
			      })
			      .then(response => response.json())
			      .then(data => {
			          if (data.error) {
			              alert("로그인 정보가 유효하지 않습니다.");
			          } else {
			              document.getElementById("username").innerText = `사용자 username: ${data.username}`;
			              document.getElementById("email").innerText = `이메일: ${data.email}`;
			          }
			      })
			      .catch(error => console.error("Error:", error));
			  }
			
          fetch("/api/user", {
              method: "GET",
              headers: {
                  "Authorization": "Bearer " + token
              }
          })
          .then(response => response.json()) // json 변환
          .then(data => {
              if (data.username) {
                  console.log(`✅ 로그인됨: ${data.username}`);
                  document.getElementById("username").innerText = `반갑습니다 ${data.username}님 `; // 사용자 이름 표시

                  loginBtn.style.display = "none"; // 로그인 버튼 숨기기
                  registerBtn.style.display="none";
				  logoutBtn.style.display = "inline-block"; // 로그아웃 버튼 보이기
                  myPageBtn.style.display = "inline-block"; // 마이페이지 버튼 보이기
              } else {
                  console.log("🚨 사용자 정보 없음, 로그인 필요");
                  loginBtn.style.display = "inline-block";
                  logoutBtn.style.display = "none";
                  myPageBtn.style.display = "none";
              }
          })
          .catch(error => {
              console.error("사용자 정보 가져오기 실패", error);
              loginBtn.style.display = "inline-block";
              logoutBtn.style.display = "none";
              myPageBtn.style.display = "none";
          });
		  
      });
	  

	  function getUserInfo() {
	      const token = localStorage.getItem("token");

	      if (!token) {
	          alert("로그인이 필요합니다.");
	          window.location.href = "/user/login";
	          return;
	      }

	      fetch("/api/mypage", {
	          method: "GET",
	          headers: {
	              "Authorization": "Bearer " + token,
	              "Content-Type": "application/json"
	          }
	      })
	      .then(response => {
	          if (!response.ok) {
	              return response.text().then(text => {
	                  console.error("Error response:", text);
	                  throw new Error('인증 실패');
	              });
	          }
	          return response.json();
	      })
	      .then(data => {
	          console.log("✅ 사용자 정보:", data);
	          localStorage.setItem("userInfo", JSON.stringify(data)); // 🔹 로컬스토리지에 저장
	          window.location.href = "/user/mypage"; // ✅ 페이지 이동
	      })
	      .catch(error => {
	          console.error("🚨 오류 발생:", error);
	          alert("사용자 정보를 불러올 수 없습니다.");
	          window.location.href = "/user/login";
	      });
	  }
