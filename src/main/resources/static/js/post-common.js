// 메인 이미지 업로드
$(document).on('change', '.recipe-image', function () {
    const file = this.files[0];
    const preview = $(this).siblings('.preview-image');

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        $.ajax({
            url: '/api/v1/upload-image',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                preview.attr('src', response.s3Url).removeClass('d-none');
                $('#mainImageId').val(response.id);
                $('#mainImageUrl').val(response.s3Url);
            }
        });
    }
});

// 조리 순서 이미지 업로드
$(document).on('change', '.step-image', function () {
    const file = this.files[0];
    const preview = $(this).siblings('.preview-image');
    const hiddenUrlInput = $(this).siblings('.stepImageUrl');
    const hiddenIdInput = $(this).siblings('.stepImageId');

    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        $.ajax({
            url: '/api/v1/upload-image',
            type: 'POST',
            data: formData,
            processData: false,
            contentType: false,
            success: function (response) {
                preview.attr('src', response.s3Url).removeClass('d-none');
                hiddenUrlInput.val(response.s3Url);
                hiddenIdInput.val(response.id);
            }
        });
    }
});


// 재료 추가 및 삭제 기능
$(document).ready(function () {
    $('#add-ingredient').click(function () {
        const newRow = `
                <div class="form-row ingredient-row mb-2">
                    <div class="col">
                        <input type="text" class="form-control ingredient-name" placeholder="재료명">
                    </div>
                    <div class="col">
                        <input type="text" class="form-control ingredient-quantity" placeholder="수량">
                    </div>
                    <div class="col">
                        <input type="text" class="form-control ingredient-unit" placeholder="단위">
                    </div>
                    <div class="col-auto">
                        <button type="button" class="btn btn-danger remove-btn">&times;</button>
                    </div>
                </div>`;
        $('#ingredient-container').append(newRow);
    });


    $(document).on('click', '.remove-btn', function () {
        $(this).closest('.ingredient-row').remove();
    });

    // 요리 순서 추가 기능
    $('#add-step').click(function () {
        const stepCount = $('.step-row').length + 1;
        const newStep = `
        <div class="form-group step-row mb-4">
            <label>Step ${stepCount}</label>
            <div class="image-upload">
                <textarea class="form-control step-description mb-2" placeholder="예) 다음 요리 단계를 입력해주세요."></textarea>
                <label class="upload-btn">
                    <input type="file" class="step-image" accept="image/*">
                    <img src="https://placehold.co/150x150" alt="사진 미리보기" class="preview-image d-none">
                    <input type="hidden" class="stepImageUrl">
                    <input type="hidden" class="stepImageId">
                </label>
            </div>
        </div>`;
        $('#step-container').append(newStep);
    });
});
