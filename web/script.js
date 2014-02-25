/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

window.onload = fun1;

function fun1() {
}

function expandFilters(tableIdNo) {
    var tableElement = document.getElementById("t"+tableIdNo);
    var filterBoxElement = document.getElementById("filters"+tableIdNo);
    if (tableElement.checked) {
        filterBoxElement.style.display="block";
    } else {
        filterBoxElement.style.display="none";
    }
}

function expandSubTables(tableIdNo) {
    var tableElement = document.getElementById("t"+tableIdNo);
    var subTableBoxElement = document.getElementById("subtbl"+tableIdNo);
    if (tableElement.checked) {
        subTableBoxElement.style.display="block";
    } else {
        subTableBoxElement.style.display="none";
    }
}

function expandBills(tableIdNo) {
    var tableIdNoStr = tableIdNo.toString();
    var rawTableIdNo;
    if (tableIdNoStr.charAt(tableIdNoStr.length-1) == "a") {
        rawTableIdNo = tableIdNoStr.substring(0, tableIdNoStr.length-1);
    } else {
        rawTableIdNo = tableIdNoStr;
    }
    var billsTableElement = document.getElementById("t"+rawTableIdNo);
    var lawsTableElement = document.getElementById("t"+rawTableIdNo+"a");
    var filterBoxElement = document.getElementById("filters"+rawTableIdNo);
    var billsTableBoxElement = document.getElementById("subtbl"+rawTableIdNo);
    var lawsTableBoxElement = document.getElementById("subtbl"+rawTableIdNo+"a");
    if (billsTableElement.checked || lawsTableElement.checked) {
        if (billsTableElement.checked) {
            billsTableBoxElement.style.display="block";
        } else {
            billsTableBoxElement.style.display="none";
        }
        if (lawsTableElement.checked) {
            lawsTableBoxElement.style.display="block";
        } else {
            lawsTableBoxElement.style.display="none";
        }
        filterBoxElement.style.display="block";
    } else {
        billsTableBoxElement.style.display="none";
        lawsTableBoxElement.style.display="none";
        filterBoxElement.style.display="none";
    }
}

function expandBudget(tableIdNo) {
    var spendingTableElement = document.getElementById("t"+tableIdNo+"A");
    var balanceTableElement = document.getElementById("t"+tableIdNo+"B");
    var filterBoxElement = document.getElementById("filters"+tableIdNo);
    var subTableBoxElement = document.getElementById("subtbl"+tableIdNo+"B");
    if (spendingTableElement.checked || balanceTableElement.checked) {
        filterBoxElement.style.display="block";
    } else {
        filterBoxElement.style.display="none";
    }
    if (balanceTableElement.checked) {
        subTableBoxElement.style.display="block";
    } else {
        subTableBoxElement.style.display="none";
    }
}

function expandSubtopics(topicNo, button) {
    var subtopicListElement = document.getElementById("s"+topicNo);
    var subtopicBoxElement = document.getElementById("x"+topicNo);
    if (subtopicListElement.style.display != "block"){
        subtopicListElement.style.display="block";
        button.value = "Contract subtopics";
    } else {
        subtopicListElement.style.display="none";
        button.value = "Expand subtopics";
    }
}

function expandNote(tableIdNo) {
    var tableElement = document.getElementById("t"+tableIdNo);
    var noteElement = document.getElementById("note"+tableIdNo);
    if (tableElement.checked) {
        noteElement.style.display="block";
    } else {
        noteElement.style.display="none";
    }
}
