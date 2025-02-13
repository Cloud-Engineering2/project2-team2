document.addEventListener("DOMContentLoaded", function () {
    const editEmailBtn = document.getElementById("editEmailBtn");
    const emailModal = new bootstrap.Modal(document.getElementById("emailModal"));
    const saveEmailBtn = document.getElementById("saveEmailBtn");

    // 이메일 수정 버튼 클릭 시 모달 열기
    editEmailBtn.addEventListener("click", function () {
        const currentEmail = document.getElementById("email").value;
        console.log("Current email:", currentEmail); // 이메일 값 로그로 확인
        if (!currentEmail) {
            alert("이메일이 없습니다.");
            return;
        }
        document.getElementById("newEmail").value = currentEmail; // 모달에 이메일 값 설정
        emailModal.show(); // 모달 열기
    });

    // 이메일 저장 버튼 클릭 시 수정된 이메일 값 서버에 전송
    saveEmailBtn.addEventListener("click", function () {
        const newEmail = document.getElementById("newEmail").value.trim();

        if (!newEmail) {
            alert("이메일을 입력해 주세요.");
            return;
        }

        const updatedData = {
            username: document.getElementById("username").value,
            email: newEmail
        };

        fetch("/user/mypage/checkSession", {
            method: "GET",
            credentials: "same-origin"
        })
            .then(response => {
                if (!response.ok) {
                    alert("로그인이 필요합니다.");
                    window.location.href = "/user/login";
                    return;
                }

                fetch("/user/mypage/update", {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(updatedData)
                })
                    .then(response => {
                        if (response.ok) {
                            alert("이메일이 성공적으로 업데이트되었습니다.");
                            document.getElementById("email").value = newEmail; // 이메일 필드 업데이트
                            emailModal.hide(); // 모달 닫기
                        } else {
                            return response.text().then(text => {
                                throw new Error(text);
                            });
                        }
                    })
                    .catch(error => {
                        console.error("Error:", error);
                        alert("이메일 업데이트 실패: " + error.message);
                    });
            })
            .catch(error => {
                console.error("Session Check Error:", error);
                alert("로그인 상태를 확인할 수 없습니다.");
                window.location.href = "/user/login";
            });
    });

    // 비밀번호 변경 버튼 클릭 시 모달 열기
    const editPasswordBtn = document.getElementById("editPasswordBtn");
    const passwordModal = new bootstrap.Modal(document.getElementById("passwordModal"));
    const savePasswordBtn = document.getElementById("savePasswordBtn");

    editPasswordBtn.addEventListener("click", function () {
        passwordModal.show();
    });

    // 비밀번호 저장 버튼 클릭 시
    savePasswordBtn.addEventListener("click", function () {
        const currentPassword = document.getElementById("currentPassword").value.trim();
        const newPassword = document.getElementById("newPassword").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();

        if (!currentPassword || !newPassword || !confirmPassword) {
            alert("모든 필드를 입력해 주세요.");
            return;
        }

        if (newPassword !== confirmPassword) {
            alert("새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return;
        }

        const passwordData = {
            username: document.getElementById("username").value,
            currentPassword: currentPassword,
            newPassword: newPassword
        };

        fetch("/user/mypage/checkSession", {
            method: "GET",
            credentials: "same-origin"
        })
            .then(response => {
                if (!response.ok) {
                    alert("로그인이 필요합니다.");
                    window.location.href = "/user/login";
                    return;
                }

                fetch("/user/mypage/changePassword", {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(passwordData)
                })
                    .then(response => {
                        if (response.ok) {
                            alert("비밀번호가 성공적으로 변경되었습니다.");
                            passwordModal.hide();
                        } else {
                            return response.text().then(text => {
                                throw new Error(text);
                            });
                        }
                    })
                    .catch(error => {
                        console.error("Error:", error);
                        alert("비밀번호 변경 실패: " + error.message);
                    });
            })
            .catch(error => {
                alert("로그인 상태를 확인할 수 없습니다.");
                window.location.href = "/user/login";
            });
    });


});