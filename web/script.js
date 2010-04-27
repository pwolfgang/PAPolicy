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
    var billsTableElement = document.getElementById("t"+tableIdNo);
    var lawsTableElement = document.getElementById("t"+tableIdNo+"a");
    var filterBoxElement = document.getElementById("filters"+tableIdNo);
    var subTableBoxElement = document.getElementById("subtbl"+tableIdNo);
    if (billsTableElement.checked || lawsTableElement.checked) {
        subTableBoxElement.style.display="block";
        filterBoxElement.style.display="block";
    } else {
        subTableBoxElement.style.display="none";
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

function expandSubtopics(topicNo) {
    var subtopicListElement = document.getElementById("s"+topicNo);
    var subtopicBoxElement = document.getElementById("x"+topicNo);
    if (subtopicBoxElement.checked) {
        subtopicListElement.style.display="block";
    } else {
        subtopicListElement.style.display="none";
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
