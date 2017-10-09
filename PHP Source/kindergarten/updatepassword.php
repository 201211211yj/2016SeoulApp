<?php

$con=mysqli_connect("localhost","--------","--------","--------");

mysqli_set_charset($con,"utf8");
  

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$email = $_POST['email'];
$password = $_POST['password'];
$new_password = $_POST['new_password'];

$res = mysqli_query($con, "
SELECT * FROM user WHERE email=\"$email\" AND pswrd = \"$password\";
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('email'=>$row[0]));
}


if($result){
	$result = mysqli_query($con, "
	UPDATE user SET pswrd = \"$new_password\" WHERE email = \"$email\";
	");
	echo json_encode($result);
  }  
  else{  
    echo json_encode("failure");
  }  
  
mysqli_close($con);  
?> 

