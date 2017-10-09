<?php
$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$id = $_POST['id'];

$res = mysqli_query($con, "
SELECT id,name,kindertype,childlimit,teacher,address,phone,room,park,cctv,bus,hompage
FROM kindergarten
WHERE id = '$id';
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('id'=>$row[0],'name'=>$row[1],'kindertype'=>$row[2], 'childlimit'=>$row[3], 'teacher'=>$row[4], 'address'=>$row[5], 'phone'=>$row[6], 'room'=>$row[7], 'park'=>$row[8], 'cctv'=>$row[9], 'bus'=>$row[10], 'hompage'=>$row[11]
    ));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
