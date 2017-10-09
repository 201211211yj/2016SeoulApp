<?php
$con=mysqli_connect("localhost","--------","--------","--------");

mysqli_set_charset($con,"utf8");


if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$email = $_POST['email'];
$name = $_POST['name'];
$child_name = $_POST['child_name'];

$result = mysqli_query($con, "
        UPDATE user SET name = \"$name\",child_name = \"$child_name\" WHERE email = \"$email\";
        ");

if(!$result)
	echo json_encode("failure");  

mysqli_close($con);
?>

