<?php

$con=mysqli_connect("localhost","--------","--------","--------");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

mysqli_set_charset($con,"utf8");

$kinder_id = $_POST['kinder_id'];
$date = $_POST['date'];

$res = mysqli_query($con, "
SELECT *
FROM dailymenutable
WHERE kinder_id = '$kinder_id' AND date = '$date';
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array(
'menu1'=>$row[2],'from1'=>$row[3],
'menu2'=>$row[4],'from2'=>$row[5],
'menu3'=>$row[6],'from3'=>$row[7],
'menu4'=>$row[8],'from4'=>$row[9],
'menu5'=>$row[10],'from5'=>$row[11],
'menu6'=>$row[12],'from6'=>$row[13],
'menu7'=>$row[14],'from7'=>$row[15],
'menu8'=>$row[16],'from8'=>$row[17],
'menu9'=>$row[18],'from9'=>$row[19],
'menu10'=>$row[20],'from10'=>$row[21],
'image_url'=>$row[21]
));
}

echo json_encode(array("result"=>$result));

mysqli_close($con);

?>
