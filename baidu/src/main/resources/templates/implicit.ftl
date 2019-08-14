<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<img id="img1"/>

</body>
<script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
<script>

    document.write(location.hash);
    $(document).ready(function () {
            $.ajax({
                type: "get",
                async:true,
                url: 'http://localhost:8080/resource/1',
                headers: {"Authorization": "Bearer "+location.hash.substring(1)},
                success: function (data) {
                    if(data.code===0) {
                        $("#img1").attr("src",data.data);
                    }
                },
            });

        }
    )



</script>
</html>