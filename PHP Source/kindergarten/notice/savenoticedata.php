
<?php

$con=mysqli_connect("localhost","--------","--------","--------");

mysqli_set_charset($con,"utf8");


if(mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$notice_id = $_POST['notice_id'];
$notice_title = $_POST['notice_title'];
$notice_text = $_POST['notice_text'];
$kinder_id = $_POST['kinder_id'];

$res = mysqli_query($con, "
SELECT notice_id FROM noticetable WHERE notice_id = \"$notice_id\";
");

$result = array();

while($row = mysqli_fetch_array($res)){
  array_push($result,
    array('notice_id'=>$row[0]));
}

echo json_encode(array("result"=>$result));

if($result){
        $result = mysqli_query($con, "
        UPDATE noticetable SET notice_title = \"$notice_title\",notice_text = \"$notice_text\"  WHERE notice_id=\"$notice_id\";
        ");
  }
  else{
    $result = mysqli_query($con, "
        INSERT INTO noticetable VALUES(\"$kinder_id\",\"$notice_title\",\"$notice_text\",\"\");
        ");
  }

mysqli_close($con);
?>
