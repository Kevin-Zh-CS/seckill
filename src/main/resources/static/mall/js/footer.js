function loadfooter() {
    const dom2 = "<div class=\"site-footer\" id=\"delay_footer\" th:fragment=\"footer-fragment\">\n" +
        "    <div class=\"footer-related\">\n" +
        "        <div class=\"footer-info w1100\">\n" +
        "            <div class=\"info-text w1100\">\n" +
        "                <p>电子商务系统结构课程大作业 &nbsp; | &nbsp;<strong>Powered by 张佳文</strong>&nbsp;</p>\n" +
        "                <p>Copyright © 2019-2020 Kevin Zhang All Rights Reserved.</p>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
    $('#delay_footer').append($(dom2));
}