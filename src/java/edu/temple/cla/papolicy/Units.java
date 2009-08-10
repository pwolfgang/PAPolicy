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
     COUNT, PERCENT, DOLLARS, RANK, PERCENT_CHANGE;
     public static String getTitle(Units unit) {
         switch(unit) {
             case COUNT: return "Number of Cases";
             case PERCENT: return "Percent";
             case DOLLARS: return "Dollars";
             case RANK: return "Rank";
             case PERCENT_CHANGE: return "Percent Change";
             default : return "";
         }
     }
 }
