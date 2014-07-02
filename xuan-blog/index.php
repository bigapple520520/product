<?php require_once 'files/site_set.php'; ?>
<?php require_once 'utils/an_utils.php'; ?>
<?php require_once 'files/data_common.php'; ?>
<?php require_once 'files/data_post.php'; ?>
<?php require_once 'files/data_album_action.php'; ?>
<?php 
$this_page = 'index.php';
$this_page_post_type = $constant_post_type_tjwz;

$mc_posts_tjwz = mc_posts($constant_post_type_tjwz);   //推荐文章
$mc_new_posts_all = mc_new_posts_by_num('all', 8);     //最新文章
$mc_ranking_posts_all = mc_new_posts_by_num('all', 5); //文章排行

$mc_new_alums = mc_new_albums_by_num(6);//获取最新5个相册
?>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title><?php echo $site_set['title']; ?></title>
<meta name="keywords" content="<?php echo $site_set['keywords']; ?>" />
<meta name="description" content="<?php echo $site_set['description']; ?>" />
<link href="css/base.css" rel="stylesheet">
<link href="css/index.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="js/modernizr.js"></script>
<![endif]-->
</head>
<body>
<?php include 'm_header.php'; ?>
<div class="banner">
  <section class="box">
    <ul class="texts">
        <p>狠狠打死的青春，埋葬了朦胧为谁的灵魂。</p>
        <p>找来谁弃的铲斗，掘一把土，眼看日落，寻一层梦。</p>
        <p>掩了土，锁了门，关了窗，再上陌路。</p>
    </ul>
    <div class="avatar"><a href="#"><span>徐 安</span></a> </div>
  </section>
</div>
<div class="template">
  <div class="box">
    <h3>
        <p><span>个人相册</span> 生活</p>
    </h3>
    <ul>
	    <?php
			reset($mc_new_alums);
			foreach($mc_new_alums as $key => $value){
				an_print('<li><a href="album.php?id='.$value['id'].'"  target="_blank"><img src="files/album/'.$value['id'].'/1.jpg"></a><span>'.$value['name'].'</span></li>');
			}
		?>
    </ul>
  </div>
</div>
<article>
<h2 class="title_tj">
	<p>推荐<span>文章</span></p>
</h2>
<div class="bloglist left">
	<?php
		reset($mc_posts_tjwz);
		foreach($mc_posts_tjwz as $key=>$post){
	?>
	<h3><?php an_print($post['title']); ?></h3>
	<figure><img src="<?php an_print(mc_get_post_url($post['pic_url'])); ?>"></figure>
	<ul>
		<p><?php an_print($post['summary']); ?></p>
		<a href="<?php an_print('detail.php?id='.$post['id'].'&type='.$this_page_post_type); ?>" target="_blank" class="readmore">阅读全文>></a>
	</ul>
	<p class="dateview">
		<span><?php an_print($post['date']); ?></span>
		<span>作者：<?php an_print($post['edit_name']); ?></span>
		<span><?php echo implode(',',mc_types_to_descriptions($post['tags'])); ?>：[<a href="#"><?php echo implode(',',$post['tags']); ?></a>]</span>
	</p>
	<?php } ?>
</div>
  
<aside class="right">
	<div class="weather"><iframe width="250" scrolling="no" height="60" frameborder="0" allowtransparency="true" src="http://i.tianqi.com/index.php?c=code&id=12&icon=1&num=1"></iframe></div>
	<div class="news">
		<h3><p>最新<span>文章</span></p></h3>
		<ul class="rank">
		<?php
			reset($mc_new_posts_all);
			foreach($mc_new_posts_all as $key=>$post){
				echo '<li><a href="detail.php?id='.$post['id'].'&type='.$post['types'][0].'" title="'.$post['title'].'" target="_blank">'.$post['title'].'</a></li>';
			}
		?>
		</ul>
		
		<h3 class="ph"><p>点击<span>排行</span></p></h3>
		<ul class="paih">
		<?php
			reset($mc_ranking_posts_all);
			foreach($mc_ranking_posts_all as $key=>$post){
				echo '<li><a href="detail.php?id='.$post['id'].'&type='.$post['types'][0].'" title="'.$post['title'].'" target="_blank">'.$post['title'].'</a></li>';
			}
		?>
		</ul>
		
		<h3 class="links"><p>友情<span>链接</span></p></h3>
		<ul class="website">
		    <li><a href="http://xuanner.com/bigapple" target="_blank">bigapple技术文档</a></li>
		    <li><a href="http://www.swift001.org" target="_blank">Swift001工作组</a></li>
		</ul> 
	</div>

	<!-- 百度分享 -->
	<div id="bdshare" class="bdshare_t bds_tools_32 get-codes-bdshare"><a class="bds_tsina"></a><a class="bds_qzone"></a><a class="bds_tqq"></a><a class="bds_renren"></a><span class="bds_more"></span><a class="shareCount"></a></div>
	<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=6574585" ></script> 
	<script type="text/javascript" id="bdshell_js"></script> 
	<script type="text/javascript">
		document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date()/3600000)
	</script> 
	<!-- 百度分享 -->
	<a href="<?php an_print($this_page); ?>" class="weixin"></a></aside>
</aside>
</article>
<?php include 'm_footer.php'; ?>
<script src="js/silder.js"></script>
</body>
</html>
