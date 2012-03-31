package schedutron;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class MainWindow extends JFrame {

	/**	GUID for the Window */
	private static final long serialVersionUID = 2278553101678377064L;

  // XXX: Yes, I know this is ugly, all the window's data is inside the object.

  /** The schedule currently loaded into the program */
  private Schedule schedule = new Schedule();
  /** The starting of the schedule display's range */
  private Date start = new Date();
  /** The end of the schedule display's range */
  private Date end = new Date();
  private int numIntervals;
  private int rowheight;

  private JButton catalogButton;
  private JButton addButton;
  private JButton removeButton;
  
  private JList availableList;
  private JList scheduledList;

  private JPanel panel;
  // TODO: Layout and stuff
  // TODO: Add CatalogWindow
  
  private void generateGrid()
  {
    GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0;
    c.ipadx = 64;
    DateFormatSymbols dfs = new DateFormatSymbols();
    // TODO: base this on current Calendar date and allow incrementing of weeks
    String[] weekdays = dfs.getShortWeekdays();
    for (int i = 1; i <= 7; i++ ) {
      c.gridx = i;
      String weekday = weekdays[i];
      System.out.println(weekday);
      panel.add(new JLabel(weekday), c);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
    try {
      start = sdf.parse("12:00 AM");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    Date time = start;
    c.gridx = 0;
    c.ipady = 8;
    numIntervals = 24; // TODO: Either generalize or make this standard
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);
    for ( int i = 1; i <= numIntervals; i++ ) {
      c.gridy = i;
      panel.add(new JLabel(sdf.format(calendar.getTime())), c);
      calendar.add(Calendar.HOUR, 1);
    }
    // TODO: Draw grid lines and/or alternate cell colors (using fill/labels?)
    drawCourses();
  }

  private void drawCourses() {
    if (schedule.getCourses() == null) {
      return;
    }
    Calendar calendar = Calendar.getInstance();
    for (Course course: schedule.getCourses()) {
      for (TimeBlock time : course.getTimes()) {
        String dayCodes = "SMTWRFU";
        calendar.setTime(time.getStart());
        int startHr = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(time.getEnd());
        // offset by -1 minutes so times on the hour don't fill in end hours
        // (ie. prevent an event that ends at 12 from looking like it ends at 1)
        calendar.add(Calendar.MINUTE, -1);
        // TODO: Better rounding method? Should a course that ends at 12:05 
        // render as ending at 12:00 or 1:00? Or should we change the division
        // to 30 minutes to avoid (at least for most purposes at U of L) this 
        // problem?
        int endHr = calendar.get(Calendar.HOUR_OF_DAY);
        // TODO: Use better name than c... names from online examples suck!
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = dayCodes.indexOf(time.getDay()) + 1;
        c.gridy = startHr + 1;
        c.gridheight = (endHr - startHr) + 1;
        c.fill = GridBagConstraints.BOTH;
        System.out.println(startHr + ", " + endHr + ", " + c.gridheight);
        JLabel label = new JLabel(course.getNumber(), JLabel.CENTER);
        label.setBackground(Color.GREEN); // TODO: Add color scheme?
        label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        label.setOpaque(true);
        panel.add(label, c);
      }
    }
  }

  private JLabel MapBlock(TimeBlock block) {
    // TODO: Refactor draw courses stuff to here
    String days = "SMTWRFU";
    return null;
  }

  private void generateList() {
    // clear availableList data
    // clear scheduledList data
  }

  public MainWindow() {
    // initialization
    panel = new JPanel(new GridBagLayout());
    schedule = new Schedule();
    SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
    // TODO: Remove hard-coded courses
    Course course1 = null;
    Course course2 = null;
    Course course3 = null;
    Course course4 = null;
    Course course5 = null;
    try {
      course1 = new Course("CECS 550", "Software Engineering", "MW",
        sdf.parse("5:30 PM"), sdf.parse("6:45 PM"), 3);
      course2 = new Course("ENGR 330", "Linear Algebra", "MW",
        sdf.parse("12:00 PM"), sdf.parse("12:50 PM"), 2);
      course3 = new Course("CECS 535", "Intro to Databases", "MW",
        sdf.parse("1:00 PM"), sdf.parse("2:15 PM"), 3);
      course4 = new Course("ECE 412", "Intro to Embedded Systems", "TR",
        sdf.parse("9:30 AM"), sdf.parse("10:45 AM"), 3);
      course5 = new Course("SOC 202", "Social Problems", "T",
        sdf.parse("11:00 AM"), sdf.parse("12:15 PM"), 2);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    schedule.addCourse(course1);
    schedule.addCourse(course2);
    schedule.addCourse(course3);
    schedule.addCourse(course4);
    schedule.addCourse(course5);
    generateGrid();
    generateList();
    this.add(panel);
    pack();
  }
  
  // TODO: Handle button clicks, etc.
  
  
}
