<?php require_once 'files/data_album.php'?>
<?php
$album_id = 1;

if(isset($_GET['id'])){
	$album_id = $_GET['id'];
}

if(!array_key_exists($album_id, $mc_albums)){
    echo '相册不存在';
	exit;
}

$album = $mc_albums[$album_id];
$image_descs = $album['image_descs'];
$image_file_path = 'files/album/'.$album_id;

$hostdir = dirname(__FILE__);
$filename_array = scandir($hostdir.'/files/album/'.$album['id']);
unset($filename_array[0]);
unset($filename_array[1]);
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>相册浏览</title>
<script type="text/javascript" src="js/myfocus/myfocus-2.0.1.min.js"></script>
<style type="text/css">
body { background:#8BBF5D; padding:20px;text-align:center;}
#myFocus{width:600px; height:450px;}
img{max-width:600px; max-height:450px;}

#center{ 
margin-right: auto;
margin-left: auto;
height:450px;
width:600px;
vertical-align:middle;
line-height:200px;
}
</style>

<script type="text/javascript">
//设置
myFocus.set({
	id:'myFocus',//ID
	pattern:'mF_fancy',//风格:mF_fscreen_tb,mF_games_tb,mF_kdui,mF_slide3D
	time:'10'//ID
});
</script>

</head>
<body>
<div id="center">
<div id="myFocus"><!--焦点图盒子-->
  <div class="loading"><img src="images/loading.gif" alt="请稍候..." /></div><!--载入画面(可删除)-->
  <div class="pic"><!--图片列表-->
  	<ul style="text-align:center;">
	<?php 
		foreach($filename_array as $key => $value){		
			$desc = '';
			if(array_key_exists($value, $image_descs)){
				$desc = $image_descs[$value];
			}
		    echo '<li><a href="#"><img class="image" src="'.$image_file_path.'/'.$value.'" thumb="" alt="'.$desc.'" text="'.$desc.'" /></a></li>';
		}
	?>
  	</ul>
  </div>
</div>
</div>
</body>
</html>