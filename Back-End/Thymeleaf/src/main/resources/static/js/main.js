    function form_submitAddCategory() {
    document.getElementById("addCategoryForm").submit();
}

    function form_submitEditCategory() {
    document.getElementById("editCategoryForm").submit();
}

    function form_submitAddItem() {
        document.getElementById("addItemForm").submit();
    }

function form_submitEditItem() {
    document.getElementById("editItemModalForm").submit();
}

function form_submitEditAddress() {
        document.getElementById("editAddressModalForm").submit();
}

    $(document).ready(function(){
    $('[data-bs-toggle="tooltip"]').tooltip()
    $('[data-bs-toggle="modal"]').tooltip();
});

    const insideModalCategoryEdit = document.getElementById('editCategoryModal')
    insideModalCategoryEdit.addEventListener('show.bs.modal', event => {

    const button = event.relatedTarget

    const categoryNameRecipient = button.getAttribute('data-bs-categoryName')
    const categoryPositionRecipient = button.getAttribute('data-bs-categoryPosition')
    const categoryDescriptionRecipient = button.getAttribute('data-bs-categoryDescription')

    const modalBodyInputCategoryName = insideModalCategoryEdit.querySelector('.getCategoryName input')
    const modalBodyInputCategoryPosition = insideModalCategoryEdit.querySelector('.getCategoryPosition input')
    const modalBodyInputCategoryDescription = insideModalCategoryEdit.querySelector('.getCategoryDescription textarea')
    modalBodyInputCategoryName.value = categoryNameRecipient
    modalBodyInputCategoryPosition.value = categoryPositionRecipient
    modalBodyInputCategoryDescription.value = categoryDescriptionRecipient
})

    const insideModalAddItem = document.getElementById('addItemModal')
    insideModalAddItem.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget

        const categoryNameRecipient = button.getAttribute('data-bs-categoryName')
        const itemViewRecipient = button.getAttribute('data-bs-itemView')

        const modalBodyInputCategoryName = insideModalAddItem.querySelector('.getCategoryName input')
        const modalBodyInputView = insideModalAddItem.querySelector('.getItemView input')
        modalBodyInputCategoryName.value = categoryNameRecipient
        modalBodyInputView.value = itemViewRecipient
    })

    const insideModelEditItem = document.getElementById('editItemModal')
    insideModelEditItem.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget

        const itemViewPageRecipient = button.getAttribute('data-bs-itemViewPage')
        const itemPriceRecipient = button.getAttribute('data-bs-itemPrice')
        const currentCategoryNameRecipient = button.getAttribute('data-bs-currentCategoryName')
        const itemNameRecipient = button.getAttribute('data-bs-itemName')
        const itemPositionRecipient = button.getAttribute('data-bs-itemPosition')
        const itemDescriptionRecipient = button.getAttribute('data-bs-itemDescription')


        const modalBodyInputItemViewPage = insideModelEditItem.querySelector('.getItemViewPage input')
        const modalBodyInputItemPrice = insideModelEditItem.querySelector('.getItemPrice input')
        const modalBodyInputCurrentCategoryName = insideModelEditItem.querySelector('.getCurrentCategoryName input')
        const modalBodyInputItemName = insideModelEditItem.querySelector('.getItemName input')
        const modalBodyInputItemPosition = insideModelEditItem.querySelector('.getItemPosition input')
        const modalBodyInputItemDescription = insideModelEditItem.querySelector('.getItemDescription textarea')

        modalBodyInputItemViewPage.value = itemViewPageRecipient
        modalBodyInputItemPrice.value = itemPriceRecipient
        modalBodyInputCurrentCategoryName.value = currentCategoryNameRecipient
        modalBodyInputItemName.value = itemNameRecipient
        modalBodyInputItemPosition.value = itemPositionRecipient
        modalBodyInputItemDescription.value = itemDescriptionRecipient
    })
