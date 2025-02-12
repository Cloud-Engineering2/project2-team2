// 폼 제출 시 JSON 데이터로 전송
$('#recipeForm').submit(function (e) {
    e.preventDefault();

    const mainImageChanged = $('#mainImageUrl').val() !== $('#originalMainImageUrl').val();
    const orphanedUrls = collectOrphanedUrls();

    const recipeData = {
        id: $('input[name="id"]').val(),
        title: $('#recipeTitle').val(),
        content: $('#recipeDescription').val(),
        mainImageS3URL: $('#mainImageUrl').val(),
        ingredients: JSON.stringify(collectIngredients()),  // 재료 정보
        instructions: JSON.stringify(collectInstructions()) // 요리 순서
    };

    const validationData = {
        mainImage: mainImageChanged ? { imageUrl: $('#mainImageUrl').val(), imageId: $('#mainImageId').val() || null } : null,
        stepImages: collectStepImageValidation(),  // 이미지 검증 데이터
        orphanedUrls: orphanedUrls
    };

    const finalData = {
        recipeData: recipeData,
        validationData: validationData
    };


    // JSON 데이터 전송 (Authorization 헤더 포함)
    $.ajax({
        url: '/api/v1/update-recipe',
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
            alert('레시피 수정에 실패했습니다.');
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

        instructions.push({ description, imageUrl });
    });
    return instructions;
}

// 이미지 검증 데이터 수집 함수
function collectStepImageValidation() {
    const stepImages = [];

    // 외부 hidden input에서 originalStepImageUrl 가져오기
    $('input.originalStepImageUrl').each(function (index) {
        const originalUrl = $(this).val();
        const currentUrl = $(`.step-row:eq(${index})`).find('.stepImageUrl').val();
        const imageId = $(`.step-row:eq(${index})`).find('.stepImageId').val();

        if (originalUrl !== currentUrl) {
            stepImages.push({ imageUrl: currentUrl, imageId: imageId || null });
        }
    });

    return stepImages;
}

function collectOrphanedUrls() {
    const orphanedUrls = [];

    // 기존 메인 이미지가 변경되었을 경우 고아 처리
    if ($('#originalMainImageUrl').val() && $('#originalMainImageUrl').val() !== $('#mainImageUrl').val()) {
        orphanedUrls.push($('#originalMainImageUrl').val());
    }

    // 수정된 선택자 사용 및 디버깅 추가
    $('input.originalStepImageUrl').each(function (index) {
        const originalUrl = $(this).val();
        const currentUrl = $(`.step-row:eq(${index})`).find('.stepImageUrl').val();


        if (originalUrl && originalUrl !== currentUrl) {
            orphanedUrls.push(originalUrl);
        }
    });

    return orphanedUrls;
}




$(document).ready(function () {
    // 레시피 수정: 재료 및 과정 파싱

    // 숨겨진 div에서 JSON 데이터 가져오기
    const ingredientsJson = $("#ingredients-json").text();
    const instructionsJson = $("#instructions-json").text();

    const ingredients = JSON.parse(ingredientsJson);
    const instructions = JSON.parse(instructionsJson);

    $("#ingredient-container").empty();
    $("#step-container").empty();

    // 재료 리스트 렌더링
    ingredients.forEach((ingredient) => {
        $("#ingredient-container").append(`
            <div class="form-row ingredient-row mb-2">
                <div class="col">
                    <input value="${ingredient.name}" type="text" class="form-control ingredient-name">
                </div>
                <div class="col">
                    <input value="${ingredient.quantity}" type="text" class="form-control ingredient-quantity">
                </div>
                <div class="col">
                    <input value="${ingredient.unit}" type="text" class="form-control ingredient-unit">
                </div>
                <div class="col-auto">
                    <button type="button" class="btn btn-danger remove-btn">&times;</button>
                </div>
            </div>
            `);
    });

    // 요리 순서 렌더링
    instructions.forEach((step, index) => {
        $("#step-container").append(`
                <div class="form-group step-row mb-4">
                    <label>Step ${index + 1}</label>
                    <div class="image-upload">
                        <textarea class="form-control step-description mb-2"
                                  placeholder="예) 소고기는 기름기를 떼어내고 적당한 크기로 썰어주세요.">${step.description}</textarea>
                        <label class="upload-btn">
                            <input type="file" class="step-image" accept="image/*">
                            ${
            step.imageUrl
                ? `<img src="${step.imageUrl}" alt="사진 미리보기" class="preview-image">`
                : `<img src="https://placehold.co/150x150" alt="사진 미리보기" class="preview-image d-none">`
        }
                            <input type="hidden" class="stepImageUrl" value="${step.imageUrl}">
                            <input type="hidden" class="stepImageId">
                        </label>
                    </div>
                </div>
            `);
        $("form#recipeForm").append(`
            <input type="hidden" class="originalStepImageUrl" name="originalStepImageUrl_${index}" value="${step.imageUrl}">
            `);

    });
});