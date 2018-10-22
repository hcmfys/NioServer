package org.springbus;

import java.text.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

//import de.kout.wlFxp.ftp.FtpFile;

/**
 *  Utility class
 *
 *@author     Alexander Kout
 *@created    30. Mrz 2002
 */
public class Utilities {

    /**
     *  verbose output
     */
    public static boolean debug = false;
    /**
     *  the FileWriter for the debug mode
     */
    public static FileWriter logFile = null;

    private static final String[] monthsWS = {" Jan ", " Feb ", " Mar ", " Apr ",
            " May ", " Jun ", " Jul ", " Aug ", " Sep ",
            " Oct ", " Nov ", " Dec "};
    private static final String[] months = {"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep",
            "Oct", "Nov", "Dec"};


    /**
     *  transforms a long into a String like 4KiB
     *
     *@param  size  Description of Parameter
     *@return       Description of the Returned Value
     */
    public static String humanReadable(double size) {
        DecimalFormat df = new DecimalFormat("###0.00");
        double f1;
        double f2;
        double f3;
        if ((f1 = size / 1024.0) > 1.0) {
            if ((f2 = f1 / 1024.0) > 1.0) {
                if ((f3 = f2 / 1024) > 1.0) {
                    return df.format(f3) + "GiB";
                }
                return df.format(f2) + "MiB";
            }
            return df.format(f1) + "KiB";
        }
        return df.format(size) + "B";
    }


    /**
     *  Description of the Method
     *
     *@param  size  Description of Parameter
     *@return       Description of the Returned Value
     */
    public static String humanReadable(long size) {
        return humanReadable(size * 1.0);
    }

    public static String humanReadableTime(double time) {
        DecimalFormat df = new DecimalFormat("###0");
        double min, hour, day, sec;
        sec = time *1.0;
        StringBuffer buf = new StringBuffer(50);
// minutes
        if ((min = sec /60.0) >= 1.0) {
// hours
            if ((hour = min /60.0) >= 1.0) {
// days
                if ((day = hour/24.0) >= 1.0) {
                    buf.append(df.format(day)).
                            append("d ").
                            append(df.format(hour-Math.floor(day)*24.0)).
                            append("h ").
                            append(df.format(min-Math.floor(hour)*60.0)).
                            append("min ").
                            append(df.format(sec-Math.floor(min)*60.0)).
                            append("s");
                    return buf.toString();
                }
                buf.append(df.format(hour)).
                        append("h ").
                        append(df.format(min-Math.floor(hour)*60.0)).
                        append("min ").
                        append(df.format(sec-Math.floor(min)*60.0)).
                        append("s");
                return buf.toString();
            }
            buf.append(df.format(min)).
                    append("min ").
                    append(df.format(sec-Math.floor(min)*60.0)).
                    append("s");
            return buf.toString();
        }
        buf.append(df.format(sec)).append("s");
        return buf.toString();
    }

    public static String humanReadableTime2(double time) {
        DecimalFormat df = new DecimalFormat("00");
        double min, hour, day, sec;
        sec = time *1.0;
        StringBuffer buf = new StringBuffer(50);
// minutes
        if ((min = sec /60.0) >= 1.0) {
// hours
            if ((hour = min /60.0) >= 1.0) {
// days
                if ((day = hour/24.0) >= 1.0) {
                    buf.append(df.format(day)).
                            append("d:").
                            append(df.format(hour-Math.floor(day)*24.0)).
                            append(":").
                            append(df.format(min-Math.floor(hour)*60.0)).
                            append(":").
                            append(df.format(sec-Math.floor(min)*60.0));
                    return buf.toString();
                }
                buf.append(df.format(hour)).
                        append(":").
                        append(df.format(min-Math.floor(hour)*60.0)).
                        append(":").
                        append(df.format(sec-Math.floor(min)*60.0));
                return buf.toString();
            }
            buf.append(df.format(min)).
                    append(":").
                    append(df.format(sec-Math.floor(min)*60.0));
            return buf.toString();
        }
        buf.append(new DecimalFormat("#0").format(sec));
        return buf.toString();
    }

    /**
     *  Heapsort "with bottom-up linear search" algorithm
     *
     *@param  list  must be a list of Strings
     */
    public static void sortList(String list[]) {
// Heap creation
        int n = list.length;
        for (int i = n / 2; i > 0; i--) {
            reheap(list, i, n);
        }
        for (int m = n; m > 0; m--) {
            String t = list[0];
            list[0] = list[m - 1];
            list[m - 1] = t;
            reheap(list, 1, m - 1);
        }
    }


    /**
     *  reheap method for heapsort
     *
     *@param  array  String array to be sorted
     *@param  root   position of root
     *@param  end    position of end
     */
    private static void reheap(String[] array, int root, int end) {
        int[] stack = new int[new Double(Math.log(array.length) / Math.log(2)).intValue() + 10];
        int s = 0;
        int pos = root;
        stack[s++] = pos;
        while (2 * pos <= end) {
            if (2 * pos + 1 > end) {
                stack[s++] = 2 * pos;
                break;
            }
            if (array[2 * pos - 1].compareToIgnoreCase(array[2 * pos]) > 0) {
                stack[s++] = 2 * pos;
            } else {
                stack[s++] = 2 * pos + 1;
            }
            pos = stack[s - 1];
        }
        pos = root;
        for (int i = s - 1; i >= 0; i--) {
            if (array[stack[i] - 1].compareToIgnoreCase(array[root - 1]) > -1) {
                pos = stack[i];
                s = i + 1;
                break;
            }
        }

        String temp = array[root - 1];
        for (int i = 1; i < s; i++) {
            array[stack[i] / 2 - 1] = array[stack[i] - 1];
        }
        array[pos - 1] = temp;
    }


/**
 *  Heapsort "with linear bottom-up search" algorithm
 *
 *@param  list    FtpFile array which is going to be sorted
 *@param  sortBy  Description of the Parameter
 */
//	public static void sortFiles(Vector list, String sortBy, boolean prio, Vector prioList) {
//	// Heap creation
//	int n = list.size();
//	for (int i = n / 2; i > 0; i--) {
//	reheap(list, i, n, sortBy, prio, prioList);
//	}
//	for (int m = n; m > 0; m--) {
//	FtpFile t = (FtpFile) list.elementAt(0);
//	list.setElementAt(list.elementAt(m - 1), 0);
//	list.setElementAt(t, m-1);
//	reheap(list, 1, m - 1, sortBy, prio, prioList);
//	}
//	}


/**
 *  reheap method for heapsort
 *
 *@param  array   array to be sorted
 *@param  root    position of root
 *@param  end     position of end
 *@param  sortBy  Description of the Parameter
 */
//	private static void reheap(Vector array, int root, int end, String sortBy, boolean prio, Vector prioList) {
//	int[] stack = new int[new Double(Math.log(array.size()) / Math.log(2)).intValue() + 10];
//	int s = 0;
//	int pos = root;
//	stack[s++] = pos;
//	while (2 * pos <= end) {
//	if (2 * pos + 1 > end) {
//	stack[s++] = 2 * pos;
//	break;
//	}
//	if (compareFiles(array.elementAt(2 * pos - 1), array.elementAt(2 * pos), sortBy, prio, prioList) > 0) {
//	stack[s++] = 2 * pos;
//	} else {
//	stack[s++] = 2 * pos + 1;
//	}
//	pos = stack[s - 1];
//	}
//	pos = root;
//	for (int i = s - 1; i >= 0; i--) {
//	if (compareFiles(array.elementAt(stack[i] - 1), array.elementAt(root - 1), sortBy, prio, prioList) > -1) {
//	pos = stack[i];
//	s = i + 1;
//	break;
//	}
//	}
//
//	Object temp = array.elementAt(root - 1);
//	for (int i = 1; i < s; i++) {
//	array.setElementAt(array.elementAt(stack[i] - 1), stack[i] / 2 - 1);
//	}
//	array.setElementAt(temp, pos - 1);
//	}


    /**
     *  compare method for sorting
     *
     *@param  f1      first file
     *@param  f2      second file
     *@param  sortBy  Description of the Parameter
     *@return         result of compareToIgnoreCase
     */
//	private static int compareFiles(Object o1, Object o2, String sortBy,boolean prio, Vector prioList) {
//	int ret;
//	FtpFile f1 = (FtpFile) o1;
//	FtpFile f2 = (FtpFile) o2;
//	if (prio) {
//	int m1 = matches(prioList, f1.getName()), m2 = matches(prioList, f2.getName());
//	if (m1 != -1 || m2 != -1) {
//	if (m1 > m2) {
//	return -1;
//	}
//	else if (m2 > m1) {
//	return 1;
//	}
//	}
//	}
//	if (f1.isDirectory() && !f2.isDirectory()) {
//	return -1;
//	}
//	if (!f1.isDirectory() && f2.isDirectory()) {
//	return 1;
//	}
//	if (sortBy.equals("Name")) {
//	return f1.getName().compareToIgnoreCase(f2.getName());
//	} else if (sortBy.equals("IName")) {
//	return -f1.getName().compareToIgnoreCase(f2.getName());
//	} else if (sortBy.equals("Size")) {
//	ret = (int) (f1.getSize() - f2.getSize());
//	} else if (sortBy.equals("ISize")) {
//	ret = (int) (f2.getSize() - f1.getSize());
//	} else if (sortBy.equals("Date")) {
//	if (f1.getDate().indexOf("/") == 2 && f2.getDate().indexOf("/") > 2) {
//	return 1;
//	} else if (f1.getDate().indexOf("/") > 2 && f2.getDate().indexOf("/") == 2) {
//	return -1;
//	}
//	ret = f1.getDate().compareToIgnoreCase(f2.getDate());
//	} else if (sortBy.equals("IDate")) {
//	if (f1.getDate().indexOf("/") == 2 && f2.getDate().indexOf("/") > 2) {
//	return -1;
//	} else if (f1.getDate().indexOf("/") > 2 && f2.getDate().indexOf("/") == 2) {
//	return 1;
//	}
//	ret = -f1.getDate().compareToIgnoreCase(f2.getDate());
//	} else {
//	return f1.getName().compareToIgnoreCase(f2.getName());
//	}
//	if (ret == 0) {
//	ret = f1.getName().compareToIgnoreCase(f2.getName());
//	}
//	return ret;
//	}

    private static int matches(Vector v, String s) {
        for (int i = 0; i < v.size(); i++) {
            try {
                if (Pattern.matches(((String) v.elementAt(i)).toLowerCase(), s.toLowerCase())) {
                    return (v.size()-i);
                }
            } catch (Exception e) {}
        }
        return -1;
    }


    /**
     *  print method which writes to a file if debug is true
     *
     *@param  s  String to be printed
     */
    public static void print(String s) {
        System.out.println(s);
        if (debug) {
            String settings = System.getProperty("user.home", ".") + File.separator + ".wlFxp";
            System.out.print(s);
            try {
                if (logFile == null) {
                    if (!new File(settings).isDirectory()) {
                        new File(settings).mkdir();
                    }
                    logFile = new FileWriter(settings + File.separator + "log.txt", true);
                }
                logFile.write(s);
                logFile.flush();
            } catch (IOException e) {
                System.err.println(e.toString());
            }
        }
    }


    /**
     *  saves the stack trace of an exception
     *
     *@param  e  Description of the Parameter
     */
    public static void saveStackTrace(Exception e) {
        String settings = System.getProperty("user.home", ".") + File.separator + ".wlFxp" + File.separator + "logs";
        try {
            if (!new File(settings).isDirectory()) {
                new File(settings).mkdirs();
            }
            FileWriter exceptionFile = new FileWriter(settings + File.separator + "exception: " + System.currentTimeMillis() / 1000);
            StackTraceElement[] t = e.getStackTrace();
            exceptionFile.write(e.toString() + "\n");
            for (int i = 0; i < t.length; i++) {
                exceptionFile.write(t[i].toString() + "\n");
            }
            exceptionFile.flush();
        } catch (IOException ex) {
            System.err.println(ex.toString());
        }
    }


/**
 *  parses the output of a list command into an array of FtpFiles
 *
 *@param  output  output of a LIST
 *@param  ftpDir  directory of the LIST
 *@return         array of FtpFiles
 */
//	public static Vector parseList(String output, String ftpDir) {
//	String[] completeList = split(output, "\r\n");
//	//	FtpFile[] tmpfiles = new FtpFile[completeList.length];
//	Vector files = new Vector(completeList.length, 100);
//	FtpFile tmp;
//	int k = 0;
//	int index;
//	for (int i = 0; i < completeList.length; i++) {
////	if (!Pattern.matches("([A-Za-z][A-Za-z][A-Za-z]) .[0-9] [0-9 ][0-9]", completeList[i]))
//	// ...
//	index = -1;
//	int j = 0;
//	int tindex = 0;
//	while (j < monthsWS.length) {
//	tindex = completeList[i].indexOf(monthsWS[j]);
//	if (tindex != -1 && (index == -1 || tindex < index)) {
//	index = tindex;
//	}
//	j++;
//	}
//	if (index == -1) {
//	continue;
//	}
////	System.out.println(index);
//	tmp = new FtpFile("");
//	files.addElement(tmp);
//	if (completeList[i].indexOf(" -> ") != -1) {
//	completeList[i] = completeList[i].substring(0, completeList[i].indexOf(" -> "));
//	}
//	tmp.setName(completeList[i].substring(
//	completeList[i].substring(index + 10,
//	completeList[i].length()).indexOf(" ")
//	+ 11 + index,
//	completeList[i].length()));
//	// if the server outputs the "." or ".." with list
//	if (tmp.getName().equals(".")
//	|| tmp.getName().equals("..")) {
//	files.removeElementAt(files.size()-1);
//	continue;
//	}
//	tmp.setSize(Long.parseLong(completeList[i].substring(completeList[i].substring(0, index).lastIndexOf(" ") + 1, index)));
//	tmp.setMode(completeList[i].substring(0, 10));
//	tmp.setFtpMode(true);
//	tmp.setDate(parseDate(completeList[i].substring(index, index + 13)));
//	if (ftpDir.equals("/"))
//	tmp.setAbsolutePath(ftpDir + tmp.getName());
//	else
//	tmp.setAbsolutePath(ftpDir +"/"+tmp.getName());
//	k++;
//	}
//	return files;
//	}


    /**
     *  parses the dates of the LIST output into good looking Strings
     *
     *@param  input  Description of the Parameter
     *@return        Description of the Return Value
     */
    private static String parseDate(String input) {
        String[] tdate = split(input, " ");
        String[] date = new String[3];
        int k = 0;
        for (int i = 0; i < tdate.length; i++) {
            if (tdate[i].equals("")) {
                continue;
            }
            date[k++] = tdate[i];
        }
        StringBuffer ret = new StringBuffer(30);
        for (int j = 0; j < 12; j++) {
            if (date[0].equals(months[j])) {
                if (j < 9) {
//	ret = "0" + (j + 1);
                    ret.append("0").append(j + 1);
                } else {
//	ret = (j + 1) + "";
                    ret.append(j + 1);
                }
                break;
            }
        }
        int t = Integer.parseInt(date[1]);
        if (t < 10) {
//	ret += "/0" + t;
            ret.append("/0").append(t);
        } else {
//	ret += "/" + t;
            ret.append("/").append(t);
        }
        if (date[2].indexOf(":") != -1) {
//	ret += " " + date[2];
            ret.append(" ").append(date[2]);
        } else {
//	ret = date[2] + "/" + ret;
            String tmp = ret.toString();
            ret.delete(0, ret.length());
            ret.append(date[2]).append("/").append(tmp);
        }
        return ret.toString();
    }


    /**
     *  parses the dates of local files into good looking Strings
     *
     *@param  date  Description of the Parameter
     *@return       Description of the Return Value
     */
    public static String parseDate(long date) {
        GregorianCalendar cal = new GregorianCalendar();
        int curYear;
        cal.setTime(new Date());
        curYear = cal.get(Calendar.YEAR);
        cal.setTime(new Date(date));
        StringBuffer ret = new StringBuffer(30);
        StringBuffer month = new StringBuffer(2);
        month.append(cal.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month.insert(0, "0");
        }
        StringBuffer day = new StringBuffer(2);
        day.append(cal.get(Calendar.DATE));
        if (day.length() == 1) {
            day.insert(0, "0");
        }
        StringBuffer hour = new StringBuffer(2);
        hour.append(cal.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1) {
            hour.insert(0, "0");
        }
        StringBuffer minute = new StringBuffer(2);
        minute.append(cal.get(Calendar.MINUTE));
        if (minute.length() == 1) {
            minute.insert(0, "0");
        }
        if (curYear > cal.get(Calendar.YEAR)) {
//	ret = "" + cal.get(Calendar.YEAR);
            ret.append(cal.get(Calendar.YEAR)).append("/").append(month).append("/").append(day);
        } else {
//	ret = "" + month;
            ret.append(month).append("/").append(day).append(" ").append(hour).append(":").append(minute);
        }
        return ret.toString();
    }


    /**
     *  rewrite of the split method from java.lang.String because it
     *  makes problems with gcj
     *
     *@param  s    Description of the Parameter
     *@param  key  Description of the Parameter
     *@return      Description of the Return Value
     */
    public static String[] split(String s, String key) {
        Vector v = new Vector(100, 50);
        while (s.length() > 0 && s.indexOf(key) != -1) {
            v.addElement(s.substring(0, s.indexOf(key)));
            s = s.substring(s.indexOf(key) + key.length(), s.length());
        }
        v.addElement(s);
        while (v.size() > 0 && ((String) v.elementAt(v.size() - 1)).equals("")) {
            v.removeElementAt(v.size() - 1);
        }
        String[] t = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            t[i] = (String) v.elementAt(i);
        }
        return t;
    }


    /**
     *  no more errors with parsing ints with this method
     *
     *@param  s  Description of the Parameter
     *@return    Description of the Return Value
     */
    public static int parseInt(String s) {
        StringBuffer b = new StringBuffer(s.length());
        for (int i = 0; i < s.length(); i++) {
            if (Pattern.matches("[-0-9]", s.substring(i, i + 1))) {
                b.append(s.substring(i, i + 1));
            }
        }
        return Integer.parseInt(b.toString());
    }
}
