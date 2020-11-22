<?php
    $con = mysqli_connect("localhost", "moapp", "wodnjs13!", "moapp");
    mysqli_query($con,'SET NAMES utf8');

    $washer_num = $_POST["washer_num"];
    
    $statement = mysqli_prepare($con, "update Washer set washer_state = 0, end_time = null where washer_num=?");
    mysqli_stmt_bind_param($statement, "s", $washer_num);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $usr_num, $password, $usr_name);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);

?>
