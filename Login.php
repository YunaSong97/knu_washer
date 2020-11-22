<?php
    $con = mysqli_connect("localhost", "moapp", "wodnjs13!", "moapp");
    mysqli_query($con,'SET NAMES utf8');

    $usr_num = $_POST["usr_num"];
    $password = $_POST["password"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM USER WHERE usr_num = ? AND password = ?");
    mysqli_stmt_bind_param($statement, "ss", $usr_num, $password);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $usr_num, $password, $usr_name);

    $response = array();
    $response["success"] = false;
 
    while(mysqli_stmt_fetch($statement)) {
        $response["success"] = true;
        $response["usr_num"] = $user_num;
        $response["password"] = $password;
        $response["usr_name"] = $usr_name;     
    }

    echo json_encode($response);

?>
