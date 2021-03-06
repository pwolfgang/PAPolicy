/* 
 * Copyright (c) 2018, Temple University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * All advertising materials features or use of this software must display 
 *   the following  acknowledgement
 *   This product includes software developed by Temple University
 * * Neither the name of the copyright holder nor the names of its 
 *   contributors may be used to endorse or promote products derived 
 *   from this software without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
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
