
// 폼 제출 시 JSON 데이터로 전송
$('#recipeForm').submit(function (e) {
    e.preventDefault();

    const recipeData = {
        title: $('#recipeTitle').val(),
        content: $('#recipeDescription').val(),
        mainImageS3URL: $('#mainImageUrl').val(),
        ingredients: JSON.stringify(collectIngredients()),  // 재료 정보
        instructions: JSON.stringify(collectInstructions()) // 요리 순서
    };

    const validationData = {
        mainImage: { imageUrl: $('#mainImageUrl').val(), imageId: $('#mainImageId').val() },
        stepImages: collectStepImageValidation()  // 이미지 검증 데이터
    };

    const finalData = {
        recipeData: recipeData,
        validationData: validationData
    };


    // JSON 데이터 전송 (Authorization 헤더 포함)
    $.ajax({
        url: '/api/v1/submit-recipe',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(finalData),
        success: function (response) {
            if (response.postId) {
                // postId로 상세 페이지 이동
                window.location.href = '/post/view/' + response.postId;
            } else {
                alert('레시피 저장은 성공했지만, 게시글 ID를 받지 못했습니다.');
                window.location.href = '/post/list';  // 예외 처리: 목록으로 이동
            }
        },
        error: function (xhr, status, error) {
            console.error('레시피 저장 중 오류 발생:', error);
            alert('레시피 저장에 실패했습니다.');
        }
    });
});

// 재료 정보 수집 함수
function collectIngredients() {
    const ingredients = [];
    $('.ingredient-row').each(function () {
        const name = $(this).find('.ingredient-name').val();
        const quantity = $(this).find('.ingredient-quantity').val();
        const unit = $(this).find('.ingredient-unit').val();

        ingredients.push({ name, quantity, unit });
    });
    return ingredients;
}

// 요리 순서 수집 함수
function collectInstructions() {
    const instructions = [];
    $('.step-row').each(function () {
        const description = $(this).find('.step-description').val();
        const imageUrl = $(this).find('.stepImageUrl').val();

        instructions.push({ description,  imageUrl });
    });
    return instructions;
}

// 이미지 검증 데이터 수집 함수
function collectStepImageValidation() {
    const stepImages = [];
    $('.step-row').each(function () {
        const imageId = $(this).find('.stepImageId').val();
        const imageUrl = $(this).find('.stepImageUrl').val();

        stepImages.push({ imageId: imageId, imageUrl: imageUrl });
    });
    return stepImages;
}
