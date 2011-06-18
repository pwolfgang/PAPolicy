/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

/**
 *
 * @author Paul Wolfgang
 */
 public enum Units {
     COUNT, PERCENT, PERCENT_OF_FILTERED, PERCENT_OF_TOTAL,  DOLLARS, RANK, PERCENT_CHANGE;
     public static String getTitle(Units unit) {
         switch(unit) {
             case COUNT: return "Number of Cases";
             case PERCENT: return "Percent";
             case PERCENT_OF_FILTERED: return "Percent of Filtered";
             case PERCENT_OF_TOTAL: return "Percent of Total";
             case DOLLARS: return "Dollars";
             case RANK: return "Rank";
             case PERCENT_CHANGE: return "Percent Change";
             default : return "";
         }
     }
 }
