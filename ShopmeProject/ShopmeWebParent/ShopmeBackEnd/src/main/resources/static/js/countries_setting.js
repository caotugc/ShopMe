var buttonLoad
var dropDownCountry
var buttonAddCountry
var buttonUpdateCountry
var buttonDeleteCountry
var labelCountryName
var fieldCountryName
var fieldCountryCode

$(document).ready(function () {
    buttonLoad = $("#buttonLoadCountries");
    dropDownCountry = $("#dropDownCountries");
    buttonAddCountry = $("#buttonAddCountry");
    buttonUpdateCountry = $("#buttonUpdateCountry");
    buttonDeleteCountry = $("#buttonDeleteCountry");
    labelCountryName = $("#labelCountryName");
    fieldCountryName = $("#fieldCountryName");
    fieldCountryCode = $("#fieldCountryCode");

    buttonLoad.click(function () {
        loadCountries();
    });

    dropDownCountry.on("change", function () {
        changFormStateToSelectedCountry();
    })

    buttonAddCountry.click(function () {
        if (buttonAddCountry.val() == "Add") {
            addCountry();
        } else {
            changeFormStateToNewCountry();
        }
    })

    buttonUpdateCountry.click(function () {
        updateCountry();
    })

    buttonDeleteCountry.click(function () {
        deleteCountry();
    })
});

function deleteCountry() {
    optionValue = dropDownCountry.val();
    countryId = optionValue.split("-")[0]
    url = contextPath + "countries/delete/"+ countryId;

    $.ajax({
        type: 'DELETE',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function () {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNewCountry();
        showToastMessage("REMOVED!!")
    }).fail(function () {
        showToastMessage("ERROR!!")
    });

}

function updateCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountry.val().split("-")[0];

    jsonData = {id: countryId, name: countryName, code: countryCode};
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId) {    //countryId là do server trả về từ RestAPI json của url tương ứng
        $("#dropDownCountries option:selected").text(countryName);
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        showToastMessage("The country has been updated");
        changeFormStateToNewCountry();
    }).fail(function () {
        showToastMessage("ERROR!!")
    });
}

function addCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code: countryCode};
    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId) {    //countryId là do server trả về từ RestAPI json của url tương ứng
        selectedNewlyAddedCountry(countryId, countryCode, countryName);
        showToastMessage("New country has been added");
    }).fail(function () {
        showToastMessage("ERROR!!")
    });
}

function selectedNewlyAddedCountry(countryId, countryCode, countryName) {
    optionValue = countryId + "-" + countryCode;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryName.val("").focus();
    fieldCountryCode.val("");
}

function changeFormStateToNewCountry() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country name:");
    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function changFormStateToSelectedCountry() {
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected country:")

    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);

    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode)
}

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON) {
        dropDownCountry.empty();

        $.each(responseJSON, function (index, country) {
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });
    }).done(function () {
        buttonLoad.val("Refresh Country List");
        showToastMessage("DONE!!")
    }).fail(function () {
        showToastMessage("ERROR!!")
    });
}

function showToastMessage(message) {
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

