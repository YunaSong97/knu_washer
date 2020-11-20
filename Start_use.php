<?php
    $con = mysqli_connect("localhost", "hongdroid94", "password", "hongdroid94");
    mysqli_query($con,'SET NAMES utf8');

    $userID = $_POST["userID"];
    $endTime = $_POST["userPassword"];
    $washerNum = $_POST["userName"];

    $statement = mysqli_prepare($con, "UPDATE WASHER SET washer_state=1, last_usr_num=?,  end_time=? where washer_num=? AND washer_state=0");
    mysqli_stmt_bind_param($statement, "sssi", $userID, $endTime, $washerNum);
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;
    echo json_encode($response);
    
    $data_stream = "'".$_POST['usr_num']."','".$_POST['start_time']."','".$_POST['end_time']."'";
    $query = "INSERT INTO LOG(Data1,Data2,Data3) values (".$data_stream.")";
    $result = mysqli_query($conn, $query);
     
    if($result)
      echo "1";
    else
      echo "-1";

    mysqli_close($con); // 디비 접속 닫기
?>
