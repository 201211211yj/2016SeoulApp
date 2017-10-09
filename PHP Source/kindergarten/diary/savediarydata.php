<?php

$con=mysqli_connect("localhost","--------","--------","--------");

mysqli_set_charset($con,"utf8");
  

if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$diary = $_POST['diary'];
$date = $_POST['date'];
$kinder_id = $_POST['kinder_id'];

$res = mysqli_query($con, "
SELECT diary FROM diarytable WHERE kinder_id=\"$kinder_id\" AND date = \"$date\";
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('diary'=>$row[0]));
}  

echo json_encode(array("result"=>$result));

if($result){
	$result = mysqli_query($con, "
	UPDATE diarytable SET diary = \"$diary\" WHERE kinder_id=\"$kinder_id\" AND date = \"$date\";
	");
  }  
  else{  
    $result = mysqli_query($con, "
        INSERT INTO diarytable VALUES(\"$kinder_id\",\"$date\",\"$diary\");
        ");
  }  
  
mysqli_close($con);  
?> 
