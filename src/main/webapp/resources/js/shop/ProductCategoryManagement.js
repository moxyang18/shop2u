$(function() {
	var shopId = 1;
	var listUrl = '/shop2u/shop/listproductcategorys?shopId=' + shopId;
	var addUrl = '/shop2u/shop/addproductcategorys';
	var deleteUrl = '/shop2u/shop/removeproductcategory';

	$
			.getJSON(
					listUrl,
					function(data) {
						if (data.success) {
							var dataList = data.data;
							$('.category-wrap').html('');
							var tempHtml = '';
							dataList
									.map(function(item, index) {
										tempHtml += ''
												+ '<div class="row row-product-category now">'
												+ '<div class="col-33 product-category-name">'
												+ item.productCategoryName
												+ '</div>'
												+ '<div class="col-33">'
												+ item.priority
												+ '</div>'
												+ '<div class="col-33"><a href="#" class="button delete" data-id="'
												+ item.productCategoryId
												+ '">Delete</a></div>' + '</div>';
									});
							$('.category-wrap').append(tempHtml);
						}
					});

	function getList() {
		$
				.getJSON(
						listUrl,
						function(data) {
							if (data.success) {
								var dataList = data.data;
								$('.category-wrap').html('');
								var tempHtml = '';
								dataList
										.map(function(item, index) {
											tempHtml += ''
													+ '<div class="row row-product-category now">'
													+ '<div class="col-33 product-category-name">'
													+ item.productCategoryName
													+ '</div>'
													+ '<div class="col-33">'
													+ item.priority
													+ '</div>'
													+ '<div class="col-33"><a href="#" class="button delete" data-id="'
													+ item.productCategoryId
													+ '">Delete</a></div>'
													+ '</div>';
										});
								$('.category-wrap').append(tempHtml);
							}
						});
	}
	getList();

	$('#submit').click(function() {
		var tempArr = $('.temp');
		var productCategoryList = [];
		tempArr.map(function(index, item) {
			var tempObj = {};
			tempObj.productCategoryName = $(item).find('.category').val();
			tempObj.priority = $(item).find('.priority').val();
			if (tempObj.productCategoryName && tempObj.priority) {
				productCategoryList.push(tempObj);
			}
		});
		$.ajax({
			url : addUrl,
			type : 'POST',
			data : JSON.stringify(productCategoryList),
			contentType : 'application/json',
			success : function(data) {
				if (data.success) {
					$.toast('Submit success');
					getList();
				} else {
					$.toast('Submit failure');
				}
			}
		});
	});

	$('#new')
			.click(
					function() {
						var tempHtml = '<div class="row row-product-category temp">'
								+ '<div class="col-33"><input class="category-input category" type="text" placeholder="category name"></div>'
								+ '<div class="col-33"><input class="category-input priority" type="number" placeholder="priority"></div>'
								+ '<div class="col-33"><a href="#" class="button delete">Delete</a></div>'
								+ '</div>';
						$('.category-wrap').append(tempHtml);
					});

	$('.category-wrap').on('click', '.row-product-category.now .delete',
			function(e) {
				var target = e.currentTarget;
				$.confirm('confirm?', function() {
					$.ajax({
						url : deleteUrl,
						type : 'POST',
						data : {
							productCategoryId : target.dataset.id,
							shopId : shopId
						},
						dataType : 'json',
						success : function(data) {
							if (data.success) {
								$.toast('delete success！');
								getList();
							} else {
								$.toast('delete failed！');
							}
						}
					});
				});
			});

	$('.category-wrap').on('click', '.row-product-category.temp .delete',
			function(e) {
				console.log($(this).parent().parent());
				$(this).parent().parent().remove();

			});
});