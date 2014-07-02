<?php
require 'head.php';
require '../files/data_common.php';

$post_id          = '';
$post_state       = '';
$post_title       = '';
$post_content     = '';
$post_tags        = array();
$post_types       = array();
$post_date        = '';
$post_time        = '';
$post_edit_name   = '';
$post_can_comment = '';
$post_pic_url     = '';
$post_summary     = '';
$error_msg        = '';
$succeed          = false;
if (isset($_POST['_IS_POST_BACK_'])) {
  $post_id          = $_POST['id'];
  $post_state       = $_POST['state'];
  $post_title       = trim($_POST['title']);
  $post_edit_name   = trim($_POST['edit_name']);
  $post_content     = get_magic_quotes_gpc() ? stripslashes(trim($_POST['content'])) : trim($_POST['content']);
  $post_tags        = explode(',', trim($_POST['tags']));
  $post_types       = explode(',', trim($_POST['types']));
  $post_date        = date("Y-m-d");
  $post_time        = date("H:i:s");
  $post_can_comment  = $_POST['can_comment'];
  $post_pic_url      = $_POST['pic_url'];
  $post_summary      = $_POST['summary'];
  if ($_POST['year'] != '')
    $post_date = substr_replace($post_date, $_POST['year'], 0, 4);
  if ($_POST['month'] != '')
    $post_date = substr_replace($post_date, $_POST['month'], 5, 2);
  if ($_POST['day'] != '')
    $post_date = substr_replace($post_date, $_POST['day'], 8, 2);
  if ($_POST['hourse'] != '')
    $post_time = substr_replace($post_time, $_POST['hourse'], 0, 2);
  if ($_POST['minute'] != '')
    $post_time = substr_replace($post_time, $_POST['minute'], 3, 2);
  if ($_POST['second'] != '')
    $post_time = substr_replace($post_time, $_POST['second'], 6, 2);
	
  //标签
  $post_tags_count = count($post_tags);
  for ($i = 0; $i < $post_tags_count; $i ++) {
    $trim = trim($post_tags[$i]);
    if ($trim == '') {
      unset($post_tags[$i]);
    } else {
      $post_tags[$i] = $trim;
    }
  }
  reset($post_tags);
  
  //类型
  $post_types_count = count($post_types);
  for ($i = 0; $i < $post_types_count; $i ++) {
    $trim = trim($post_types[$i]);
    if ($trim == '') {
      unset($post_types[$i]);
    } else {
      $post_types[$i] = $trim;
    }
  }
  reset($post_types);
  
  if($post_edit_name == ''){
    $post_edit_name = '匿名客';
  }
  
  if ($post_title == '') {
    $error_msg = '文章标题不能为空';
  }
  else {
    if ($post_id == '') {
      $file_names = shorturl($post_title);
      foreach ($file_names as $file_name) {
        $file_path = '../files/posts/data/'.$file_name.'.dat';
        if (!is_file($file_path)) {
          $post_id = $file_name;
          break;
        }
      }
    }
    else {
      $file_path = '../files/posts/data/'.$post_id.'.dat';
      $data = unserialize(file_get_contents($file_path));
      $post_old_state = $data['state'];
      if ($post_old_state != $post_state) {
        $index_file = '../files/posts/index/'.$post_old_state.'.php';
        require $index_file;
        unset($mc_posts[$post_id]);
        file_put_contents($index_file,
          "<?php\n\$mc_posts=".var_export($mc_posts, true)."\n?>"
        );
      }
    }
    $data = array(
      'id'          => $post_id,
      'state'       => $post_state,
      'title'       => $post_title,
	  'edit_name'   => $post_edit_name,
	  'pic_url'     => $post_pic_url,
	  'summary'     => $post_summary,
      'tags'        => $post_tags,
	  'types'       => $post_types,
      'date'        => $post_date,
      'time'        => $post_time,
      'can_comment'  => $post_can_comment,
    );
    $index_file = '../files/posts/index/'.$post_state.'.php';
    require $index_file;
    $mc_posts[$post_id] = $data;
    uasort($mc_posts, "post_sort");
    file_put_contents($index_file,
      "<?php\n\$mc_posts=".var_export($mc_posts, true)."\n?>"
    );
    $data['content'] = $post_content;
    file_put_contents($file_path, serialize($data));
    $succeed = true;
  }
} else if (isset($_GET['id'])) {
  $file_path = '../files/posts/data/'.$_GET['id'].'.dat';
  $data = unserialize(file_get_contents($file_path));
  $post_id      = $data['id'];
  $post_state   = $data['state'];
  $post_title   = $data['title'];
  $post_edit_name   = $data['edit_name'];
  $post_pic_url     = $data['pic_url'];
  $post_summary     = $data['summary'];
  $post_content = $data['content'];
  $post_tags    = $data['tags'];
  $post_types   = $data['types'];
  $post_types   = $data['types'];
  $post_date    = $data['date'];
  $post_time    = $data['time'];
  $post_can_comment = isset($data['can_comment']) ? $data['can_comment'] : '1';
}
?>
<script type="text/javascript">
function empty_textbox_focus(target){
  if (target.temp_value != undefined && target.value != target.temp_value)
    return;
  target.temp_value = target.value;
  target.value='';
  target.style.color='#000';
}
function empty_textbox_blur(target) {
  if (target.value == '') {
    target.style.color='#888';
    target.value = target.temp_value;
  }
}
</script>
<form action="<?php echo $_SERVER['REQUEST_URI']; ?>" method="post">
  <input type="hidden" name="_IS_POST_BACK_" value=""/>
  <?php if ($succeed) { ?>
  <?php if ($post_state == 'publish') { ?>
  <div class="updated">文章已发布。 <a href="../?post/<?php echo $post_id; ?>" target="_blank">查看文章</a></div>
  <?php } else { ?>
  <div class="updated">文章已保存到“草稿箱”。 <a href="post.php?state=draft">打开草稿箱</a></div>
  <?php } ?>
  <?php } ?>
  <div class="admin_page_name">
  <?php if ($post_id == '') echo "撰写文章"; else echo "编辑文章"; ?>
  </div>
  <div style="margin-bottom:20px;">
    <input name="title" type="text" class="edit_textbox" value="<?php
    if ($post_title == "") {
      echo '在此输入标题" " style="color:#888;" onfocus="empty_textbox_focus(this)" onblur="empty_textbox_blur(this)';
    }
    else {
      echo htmlspecialchars($post_title);
    }
    ?>"/>
  </div>
  <div style="margin-bottom:20px;">
    <script language="javascript" src="xheditor/jquery.js"></script>
	<script language="javascript" src="xheditor/xheditor.js"></script>
	<script language="javascript">
		$(pageInit);
		function pageInit(){
		$('#elm1').xheditor({urlType:'abs',internalScript:true,inlineScript:true,emotPath:'xheditor/xheditor_emot/',upImgUrl:"upload.php",upImgExt:"jpg,jpeg,gif,png",onUpload:insertUpload});
				}
		function insertUpload(arrMsg){
		var i,msg;
		for(i=0;i<arrMsg.length;i++){
		msg=arrMsg[i];
		$("#uploadList").append('<option value="'+msg.id+'">'+msg.localname+'</option>');
				}
		}
	</script>
    <textarea id="elm1" name="content" style="width: 860px; height: 450px; display: none; "><?php echo htmlspecialchars($post_content); ?></textarea>
  </div>
  <div style="margin-bottom:20px;">
	摘要，显示在列表中的：<br />
	<input name="summary" type="text" class="edit_textbox" value="<?php
    if (count($post_tags) == 0) {
      echo '我是摘要';
    }
    else {
      echo htmlspecialchars($post_summary);
    }
    ?>"/>
  
    标签，多个用英语逗号(,)分隔：<br />
	<input name="tags" type="text" class="edit_textbox" value="<?php
    if (count($post_tags) == 0) {
      echo '未分类';
    }
    else {
      echo htmlspecialchars(implode(',', $post_tags));
    }
    ?>"/>
	
	文章类型，多个用英语逗号(,)分隔 (1：推荐文章、2：点点碎语、3：开源日记、4：技术杂谈)：<br />
	<input name="types" type="text" class="edit_textbox" value="<?php
    if (count($post_types) == 0) {
      echo '1';
    }
    else {
      echo htmlspecialchars(implode(',', $post_types));
    }
    ?>"/>
	
	编辑人姓名：<br />
	<input name="edit_name" type="text" class="edit_textbox" value="<?php
    if ($post_edit_name == '') {
      echo '徐安';
    }
    else {
      echo htmlspecialchars($post_edit_name);
    }
    ?>"/>
	
	文章图片地址，默认不填的话将会随机生成一张，可指定图片范围：<?php echo '1到'.$constant_can_use_postimage_num ?>
	<input name="pic_url" type="text" class="edit_textbox" value="<?php
    if ($post_edit_name == '') {
      echo 'files/posts-image/2.jpg';
    }
    else {
      echo htmlspecialchars($post_pic_url);
    }
    ?>"/>
  </div>
  <div style="margin-bottom:20px;text-align:right">
    <div style="float:left">
    时间：
    <select name="year">
      <option value=""></option>
<?php $year = substr($post_date, 0, 4); for ($i = 1990; $i <= 2030; $i ++) { ?>
      <option value="<?php echo $i; ?>" <?php if ($year == $i) echo 'selected="selected";' ?>><?php echo $i; ?></option>
<?php } ?>
    </select> -
    <select name="month">
      <option value=""></option>
<?php $month = substr($post_date, 5, 2); for ($i = 1; $i <= 12; $i ++) { $m = sprintf("%02d", $i); ?>
      <option value="<?php echo $m; ?>" <?php if ($month == $m) echo 'selected="selected";' ?>><?php echo $m; ?></option>
<?php } ?>
    </select> -
    <select name="day">
      <option value=""></option>
<?php $day = substr($post_date, 8, 2); for ($i = 1; $i <= 31; $i ++) { $m = sprintf("%02d", $i); ?>
      <option value="<?php echo $m; ?>" <?php if ($day == $m) echo 'selected="selected";' ?>><?php echo $m; ?></option>
<?php } ?>
    </select>&nbsp;
    <select name="hourse">
      <option value=""></option>
<?php $hourse = substr($post_time, 0, 2); for ($i = 0; $i <= 23; $i ++) { $m = sprintf("%02d", $i); ?>
      <option value="<?php echo $m; ?>" <?php if ($hourse == $m) echo 'selected="selected";' ?>><?php echo $m; ?></option>
<?php } ?>
    </select> :
    <select name="minute">
      <option value=""></option>
<?php $minute = substr($post_time, 3, 2); for ($i = 0; $i <= 59; $i ++) { $m = sprintf("%02d", $i); ?>
      <option value="<?php echo $m; ?>" <?php if ($minute == $m) echo 'selected="selected";' ?>><?php echo $m; ?></option>
<?php } ?>
    </select> :
    <select name="second">
      <option value=""></option>
<?php $second = substr($post_time, 6, 2); for ($i = 0; $i <= 59; $i ++) { $m = sprintf("%02d", $i); ?>
      <option value="<?php echo $m; ?>" <?php if ($second == $m) echo 'selected="selected";' ?>><?php echo $m; ?></option>
<?php } ?>
    </select> (留空为当前时间)
    </div>
    评论：
    <select name="can_comment" style="margin-right:16px;">
      <option value="1" <?php if ($post_can_comment == '1') echo 'selected="selected";'; ?>>允许</option>
      <option value="0" <?php if ($post_can_comment == '0') echo 'selected="selected";'; ?>>禁用</option>
    </select>
    状态：
    <select name="state" style="width:100px;">
      <option value="publish" <?php if ($post_state == 'publish') echo 'selected="selected"'; ?>>发布</option>
      <option value="draft" <?php if ($post_state == 'draft') echo 'selected="selected"'; ?>>草稿</option>
    </select>
    <div style="clear:both;"></div>
  </div>
  <div style="text-align:right">
    <input type="hidden" name="id" value="<?php echo $post_id; ?>"/>
    <input type="submit" name="save" value="保存" style="padding:6px 20px;"/>
  </div>
</form>
<?php require 'foot.php' ?>