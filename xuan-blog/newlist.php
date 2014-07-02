<?php require_once 'files/site_set.php'; ?>
<?php require_once 'utils/an_utils.php'; ?>
<?php require_once 'files/data_common.php'; ?>
<?php require_once 'files/data_post.php'; ?>
<?php
$current_page = 1;
$this_page_post_type = $constant_post_type_ddsy;
if(isset($_GET['page'])){
    $current_page = $_GET['page'];
}

$tag = 'all';
if(isset($_GET['tag'])){
    $tag = $_GET['tag'];
}

//以下参数分页用
$go_where = 'newlist.php';
$total_count = mc_count_posts_by_tag($constant_post_type_ddsy);
$total_page = ceil($total_count / $mc_post_per_page);

if($total_page == 0){
    $total_page = 1;
}

if($current_page < 1){
    $current_page = 1;
}else if($current_page > $total_page){
    $current_page = $total_page;
}

//显示数据
$mc_posts_ddsy = mc_posts_by_page_tag($constant_post_type_ddsy);           //文章列表
$ret_tags = mc_tags($constant_post_type_ddsy);                             //类别列表
$mc_new_posts_ddsy = mc_new_posts_by_num($constant_post_type_ddsy, 8);     //最新文章
$mc_ranking_posts_ddsy = mc_new_posts_by_num($constant_post_type_ddsy, 5); //排行文章
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php echo $site_set['title']; ?></title>
<meta name="keywords" content="<?php echo $site_set['keywords']; ?>" />
<meta name="description" content="<?php echo $site_set['description']; ?>" />
<link href="css/base.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="js/modernizr.js"></script>
<![endif]-->
</head>
<body>
<?php include 'm_header.php'; ?>
<article class="blogs">
<h1 class="t_nav">
	<span><?php an_print($site_set['tip2']);?></span>
	<a href="index.php" class="n1">回到首页</a><a href="newlist.php" class="n2">点点碎语</a>
</h1>
<div class="newblog left">
    <?php
		reset($mc_posts_ddsy);
		foreach($mc_posts_ddsy as $key=>$post){
	?>
	<h2><?php an_print($post['title']); ?></h2>
    <p class="dateview">
		<span>发布时间：<?php an_print($post['date']); ?></span>
		<span>作者：<?php an_print($post['edit_name']); ?></span>
		<span>分类：[<a href="#"><?php an_print(implode(',',$post['tags'])); ?></a>]</span>
	</p>
    <figure><img src="<?php an_print(mc_get_post_url($post['pic_url'])); ?>"></figure>
    <ul class="nlist">
        <p><?php an_print($post['summary']); ?></p>
        <a href="<?php an_print('detail.php?id='.$post['id'].'&type='.$this_page_post_type); ?>" target="_blank" class="readmore">阅读全文>></a>
    </ul>
    <div class="line"></div>
	<?php } ?>
	
    <div class="blank"></div>
    <div class="page">
		<a title="总数"><b><?php an_print($total_count); ?></b></a>
		<?php mc_goto_page(); ?>
	</div>
</div>

<!--右边区域 -->
<aside class="right">
    <!--类别-->
    <div class="rnav">
		<ul>
		<?php  
			reset($ret_tags);
			foreach($ret_tags as $key=>$tag){
				an_print('<li class="rnav'.mc_rand(1,4).'"><a href="newlist.php?tag='.$tag.'" target="_blank">'.$tag.'</a></li>');
			}
		?>
		</ul>      
    </div>
	
    <div class="news">
	    <!--最新文章-->
		<h3><p>最新<span>文章</span></p></h3>
		<ul class="rank">
		<?php
			reset($mc_new_posts_ddsy);
			foreach($mc_new_posts_ddsy as $key=>$post){
				an_print('<li><a href="detail.php?id='.$post['id'].'&type='.$this_page_post_type.'" title="'.$post['title'].'" target="_blank">'.$post['title'].'</a></li>');
			}
		?>
		</ul>
		
		<!--排行文章-->
		<h3 class="ph"><p>点击<span>排行</span></p></h3>
		<ul class="paih">
		<?php
			reset($mc_ranking_posts_ddsy);
			foreach($mc_ranking_posts_ddsy as $key=>$post){
				an_print('<li><a href="detail.php?id='.$post['id'].'&type='.$this_page_post_type.'" title="'.$post['title'].'" target="_blank">'.$post['title'].'</a></li>');
			}
		?>
		</ul>
    </div>
    <div class="visitors">
        <h3><p>最近访客</p></h3>
        <ul></ul>
    </div>
	
    <!-- 百度分享 -->
    <div id="bdshare" class="bdshare_t bds_tools_32 get-codes-bdshare">
		<a class="bds_tsina"></a><a class="bds_qzone"></a>
		<a class="bds_tqq"></a>
		<a class="bds_renren"></a>
		<span class="bds_more"></span>
		<a class="shareCount"></a>
	</div>
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