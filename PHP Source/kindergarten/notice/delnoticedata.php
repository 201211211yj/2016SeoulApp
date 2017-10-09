<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$notice_id = $_POST['notice_id'];


mysqli_set_charset($con,"utf8");


$result = mysqli_query($con, "DELETE
FROM noticetable
WHERE notice_id = \"$notice_id\""
);

if($result)
	$result = "success";
else
	$result = "failure";
echo json_encode($result);

mysqli_close($con);

?>
