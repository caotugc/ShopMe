function clearFilter() {
	window.location = moduleURL;
}
function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	
	// Kiểm tra giá trị entityId và href trước khi gán vào modal
	if (!entityId || !link.attr("href")) {
		console.error("entityId or href is missing!");
		return;
	}
	$("#yesButton").attr("href", link.attr("href"));
	$("#confirmText").text("Are you sure to delete this " + entityName + " ID " + entityId + " ?");
	
	$("#confirmModal").modal('show');
}