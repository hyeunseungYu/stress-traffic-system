$(document).ready(function () {
    alert('seats')
    alert()

    $.ajax({
        type: "GET",
        url: "/seats/{showId}",
        data: {},
        dataType: "json",
        success: function (response) {
            console.log('success')
            let seats = response['seats'];
            console.log(response)

            //todo showId 가져올 방법 생각해보기
            var showId = 1;

            for (let i = 0; i < seats.length; i++) {
                let temp_html = ``;

                if (i % 10 == 0) {
                    let div_html = `<div class="row">`
                    temp_html += div_html;
                }



                if (1 % 10 == 9) {
                    let div_html = `</div>`
                    temp_html += div_html;
                }

            }

        }
    })

});