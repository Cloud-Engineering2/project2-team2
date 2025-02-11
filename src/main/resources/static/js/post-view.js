// ********************************************************************
// 버튼 동작 관련 코드
// ********************************************************************





$(document).ready(function() {
    // =================
    // 좋아요 버튼 관련
    // =================
    let postId = $('#like-button').data('post-id');

    console.log("postId: ", postId);  // 값 확인

    if (!postId) {
        console.error("postId가 존재하지 않습니다.");
        return;
    }

    // ✅ 현재 좋아요 수 가져오기
    // $.get(`/api/v1/likes/${postId}`, function(response) {
    //     $('#like-count').text(response.likeCount);
    // });

    $('#like-button').on('click', function() {
        let likeCountElement = $('#like-count');
        let likeButton = $('#like-button');
        $.ajax({
            url: '/api/v1/likes',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ postId: postId }), // ✅ userId 없이 요청
            success: function(response) {
                likeCountElement.text(response.likeCount); // 최신 값 적용

                if (response.action === "scrapped") {
                    likeButton.removeClass('btn-outline-danger').addClass('btn-danger');  // Solid red when scrapped
                    likeButton.html('❤️ 스크랩');  // Update button text
                } else if (response.action === "unscrapped") {
                    likeButton.removeClass('btn-danger').addClass('btn-outline-danger');  // Outline when unscrapped
                    likeButton.html('❤️ 스크랩');  // Revert button text
                }

            },
            error: function(xhr, status, error) {
                if (xhr.status === 401) {
                    alert('로그인이 필요한 서비스입니다.');
                    // 필요시 로그인 페이지로 이동
                    // window.location.href = '/user/login';
                } else {
                    console.error('좋아요 처리 중 오류 발생:', error);
                    likeCountElement.text(currentCount);
                }
            }
        });
    });

    // =================
    // 삭제 버튼 관련
    // =================
    $('#delete-button').on('click', function() {
        if (confirm('정말로 이 레시피를 삭제하시겠습니까?')) {
            $.ajax({
                url: `/api/v1/delete-recipe/${postId}`,
                type: 'DELETE',
                success: function(response) {
                    alert(response.message);
                    window.location.href = '/post/list';
                },
                error: function(xhr, status, error) {
                    console.error('삭제 처리 중 오류 발생:', error);
                    alert('레시피 삭제에 실패했습니다.');
                }
            });
        }
    });

});

function goBack() {
    if (document.referrer && ( document.referrer.includes('/post/list') || document.referrer.includes('/user/myrecipe'))) {
        window.history.back();
    } else {
        window.location.href = '/post/list';
    }
}

// ********************************************************************
// 본문 로드
// ********************************************************************
$(document).ready(function() {
    // 숨겨진 div에서 JSON 데이터 가져오기
    const ingredientsJson = $('#ingredients-json').text();
    const instructionsJson = $('#instructions-json').text();

    const ingredients = JSON.parse(ingredientsJson);
    const instructions = JSON.parse(instructionsJson);

    // 재료 리스트 렌더링
    ingredients.forEach(ingredient => {
        $('#ingredient-list').append(`
                <li class="list-group-item">
                    ${ingredient.name} - ${ingredient.quantity}${ingredient.unit}
                </li>
            `);
    });

    // 조리 순서 렌더링
    instructions.forEach((step, index) => {
        // 이미지가 있는 경우와 없는 경우를 구분하여 처리
        const imageSection = step.imageUrl
            ? `<div class="col-md-4 text-center">
                <img src="${step.imageUrl}" alt="Step Image" class="img-fluid rounded square-image">
           </div>`
            : '';

        $('#instruction-list').append(`
        <div class="row mb-4">
            <!-- 텍스트 부분 -->
            <div class="${step.imageUrl ? 'col-md-8' : 'col-md-12'}">
                <h5 class="fw-bold">Step ${index + 1}</h5>
                <p>${step.description}</p>
            </div>

            ${imageSection}
        </div>
    `);
    });
});