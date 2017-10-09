<?php

$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$email = $_POST['email'];
$param = $_POST['param'];

$str = strcmp($param,"register");

if($str){
	$result = mysqli_query($con, "
	UPDATE user 
	SET ready_register = '0'
	where email = \"$email\";
	");
}
else{
	$result = mysqli_query($con, "
        UPDATE user
        SET kinder_id = NULL,ready_register = '0'
        where email = \"$email\";
        ");
}

mysqli_close($con);

?>
