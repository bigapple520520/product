<?php require_once 'files/site_set.php'; ?>
<?php require_once 'utils/an_utils.php'; ?>
<?php require_once 'files/data_common.php'; ?>
<?php require_once 'files/data_album_action.php'; ?>
<?php
$current_page = 1;
if(isset($_GET['page'])){
    $current_page = $_GET['page'];
}

$tag = 'all';
if(isset($_GET['tag'])){
    $tag = $_GET['tag'];
}

//以下参数分页用
$go_where = 'caselist.php';
$total_count = mc_albums_count($constant_post_type_ddsy);
$total_page = ceil($total_count / $mc_album_per_page);

if($total_page == 0){
    $total_page = 1;
}

if($current_page < 1){
    $current_page = 1;
}else if($current_page > $total_page){
    $current_page = $total_page;
}

//显示数据
$ret_albums = mc_albums_by_page_tag();
$ret_tags = mc_albums_tags();
$ret_new_albums = mc_new_albums_by_num(8);
$ret_ranking_albums = mc_new_albums_by_num(5);
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php an_print($site_set['title']); ?></title>
<meta name="keywords" content="<?php an_print($site_set['keywords']); ?>" />
<meta name="description" content="<?php an_print($site_set['description']); ?>" />
<link href="css/base.css" rel="stylesheet">
<link href="css/case.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="js/modernizr.js"></script>
<![endif]-->
</head>
<body>
<?php include 'm_header.php'; ?>
<article class="blogs">
<h1 class="t_nav">
	<span><?php an_print($site_set['tip4']); ?></span>
	<a href="index.php" class="n1">回到首页</a>
	<a href="caselist.php" class="n2">相册</a>
</h1>
	
<!-- 左侧相册部分 -->
<div class="caselist left">
	<ul>
	    <?php 
			reset($ret_albums);
			foreach($ret_albums as $key => $value){
				an_print('<li><a href="album.php?id='.$value['id'].'" target="_blank"><img src="files/album/'.$value['id'].'/1.jpg"></a></li>');
			}
		?>
	</ul>
	<div class="blank"></div>
	<div class="page">
		<a title="总数"><b><?php an_print($total_count); ?></b></a>
		<?php mc_album_goto_page(); ?>
	</div>
</div>

<!-- 右侧导航部分 -->
<aside class="right">
   <div class="rnav">
        <h2>相册分类</h2>
        <ul>
		    <?php
				foreach($ret_tags as $key => $value){
				    an_print('<li><a href="caselist.php?tag='.$value.'" target="_blank">'.$value.'</a></li>');
				}
			?>
		</ul>      
    </div>
	<div class="news">
	    <!--最新文章-->
		<h3><p>最新<span>相册</span></p></h3>
		<ul class="rank">
		<?php
			reset($ret_new_albums);
			foreach($ret_new_albums as $key=>$album){
				an_print('<li><a href="album.php?id='.$album['id'].'" title="'.$album['name'].'" target="_blank">'.$album['name'].'</a></li>');
			}
		?>
		</ul>
		
		<!--排行文章-->
		<h3 class="ph"><p>点击<span>排行</span></p></h3>
		<ul class="paih">
		<?php
			reset($ret_ranking_albums);
			foreach($ret_ranking_albums as $key=>$album){
				an_print('<li><a href="album.php?id='.$album['id'].'" title="'.$album['name'].'" target="_blank">'.$album['name'].'</a></li>');
			}
		?>
		</ul>
    </div>
    <div class="visitors">
        <h3><p>最近访客</p></h3>
        <ul></ul>
    </div>
	
    <!-- 百度分享 -->
    <div id="bdshare" class="bdshare_t bds_tools_32 get-codes-bdshare"><a class="bds_tsina"></a><a class="bds_qzone"></a><a class="bds_tqq"></a><a class="bds_renren"></a><span class="bds_more"></span><a class="shareCount"></a></div>
    <script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=6574585" ></script> 
    <script type="text/javascript" id="bdshell_js"></script> 
    <script type="text/javascript">
		document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000)
	</script> 
    <!-- 百度分享 -->  
</aside>
</article>
<?php include 'm_footer.php'; ?>
<script src="js/silder.js"></script>
</body>
</html>