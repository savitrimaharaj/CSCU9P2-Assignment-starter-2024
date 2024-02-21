import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * CSCU9A2 assignment Spring 2021 - STARTER CODE
 *
 * An interactive product database with graphic display
 *
 * This application allows the user to enter product sales figures,
 * and displays them with percentages, and as a pie chart.
 *
 * Various functions are available: adding, updating, deleting, clearing
 * and sorting the data.
 *
 * THIS FILE CONTAINS STARTER CODE THAT WORKS PARTIALLY BUT SOME METHODS
 * REQUIRE COMPLETING - SEE FURTHER DOWN IN THIS COMMENT
 *
 * The application  contains a 'product database' of product names and sales figures
 * - quite small in this version, but in principle it could be quite large.
 *
 * At any one time a table is displayed showing the products, their sales and each
 * product's percentage of the total sales. A coloured pie chart is also displayed
 * giving a graphic representation of the relative proportions of the product sales.
 *
 * In this starter code, some dummy data for two products is built-in to show the effect.
 *
 * Buttons are provided to allow the user:
 *  o  to add a new named product (with zero sales initially)
 *  o  to update the sales figure for a particular product
 *  o  to delete a specified product
 *  o  to clear all the sales data (but keeping the product names)
 *  o  to sort (re-order) the sales table in alphabetic order of product names
 *  o  to sort (re-order) the sales table in descending order of the sales figures
 *
 * Of course, in this exercise, the 'database' is a little unrealistic:
 * the information is simply handled within the program, whereas in a 'serious'
 * system it would, perhaps, be read in from a file or external database.
 *
 * *** WHAT NEEDS TO BE DONE: ***
 *
 * The Add new product button function is ALMOST CORRECT.
 * The core data processing methods for the other five buttons REQUIRE COMPLETING.
 *
 * You should insert YOUR student number instead of 1234567 in line 109.
 *
 * The only other work is on the METHOD BODIES BELOW line 470 (see /////////////////////////////):
 *
 * For the six buttons: You must complete the addNewProduct method,
 * and implement full method bodies for: clearAllSales, updateSales,
 * deleteProduct, sortByName and sortBySalesDescending.
 *
 * There is one additional method to be implemented, computePercentages, which is not
 * called directly when any particular button is clicked, but it will be important to
 * call it *from* the other methods when the sales data changes. Unless the sales percentages
 * are re-computed, the pie chart drawing will *not* work properly
 * - so it is advisable to complete it early.
 *
 * You MUST NOT alter any other parts of the program:
 *  o  the GUI parts are complete and correct
 *  o  the array declarations are complete and correct
 *  o  the method headers are complete and correct.
 *
 */
public class ProductDatabase extends JFrame implements ActionListener
{
    /**
     * Frame constants
     */
    private static final int FRAME_X = 200;
    private static final int FRAME_Y = 200;
    private static final int FRAME_WIDTH = 650;
    private static final int FRAME_HEIGHT = 800;

    /**
     * Display area layout constants
     */
    private final int DISPLAY_WIDTH = 600;
    private final int DISPLAY_HEIGHT = 600;
    private final int LEFT_X = 25;              // Left hand side of the table
    private final int PRODUCT_X = LEFT_X + 5;   // Start of product number column
    private final int NAME_X = PRODUCT_X + 45;  // Start of product name column
    private final int SALES_X = NAME_X + 300;   // Start of sales column
    private final int PERCENT_X = SALES_X + 50; // Start of percentage column
    private final int RIGHT_X = PERCENT_X + 50;    // The right hand side of the table
    private final int TOP_Y = 20;               // Text starts this far down from top of display area
    private final int LINE_GAP = 20;            // Line spacing in the table
    private final int KEYWIDTH = 30;            // Width of colour box key at right

    /**
     * Drawing fonts and colours
     */
    private final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    private final Font HEADING_FONT = new Font("Arial", Font.BOLD, 14);
    private final Font NORMAL_FONT = new Font("Arial", Font.PLAIN, 14);

    private Color[] chartColour;                // Colours for the pie chart wedges

    /**
     * The main launcher method
     * @param args Unused
     */
    public static void main(String[] args)
    {
        ProductDatabase frame = new ProductDatabase();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(FRAME_X, FRAME_Y);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("Product sales chart. Student no: 1234567");
        frame.createGUI();
        frame.initializeSalesData();
        frame.setVisible(true);
    }

    /**
     * The GUI components
     */
    private JTextField newProductNameEntryField;
    private JTextField productNumberEntryField;
    private JTextField productSalesEntryField;
    private JButton newProductButton;
    private JButton updateSalesButton;
    private JButton deleteProductButton;
    private JButton clearAllSalesButton;
    private JButton sortByNameButton;
    private JButton sortBySalesDescendingButton;

    private JPanel displayArea;

    /**
     * Helper method to build the GUI
     */
    private void createGUI()
    {
        // Standard window set up
        Container window = getContentPane();
        window.setLayout(new FlowLayout());
        window.setBackground(Color.lightGray);

        // Spacer to force alignment
        window.add(new JLabel("                      "));

        // newProductName entry label and text field
        JLabel newProductNameEntryLabel = new JLabel("New product name:");
        newProductNameEntryField = new JTextField(15);
        window.add(newProductNameEntryLabel);
        window.add(newProductNameEntryField);

        // Button to add new product
        newProductButton = new JButton("Add new product");
        newProductButton.addActionListener(this);
        newProductButton.setToolTipText("Enter product name first");
        window.add(newProductButton);

        // Spacer to force new line
        window.add(new JLabel("                     "));

        // productNumber entry label and text field
        JLabel productNumberEntryLabel = new JLabel("Product number:");
        productNumberEntryField = new JTextField(5);
        window.add(productNumberEntryLabel);
        window.add(productNumberEntryField);

        // productSales entry label and text field
        JLabel productSalesEntryLabel = new JLabel("Product sales:");
        productSalesEntryField = new JTextField(5);
        window.add(productSalesEntryLabel);
        window.add(productSalesEntryField);

        // Button to add new sales figure
        updateSalesButton = new JButton("Update sales");
        updateSalesButton.addActionListener(this);
        updateSalesButton.setToolTipText("Enter product number and new sales figure first");
        window.add(updateSalesButton);

        // Button to add delete product
        deleteProductButton = new JButton("Delete product");
        deleteProductButton.addActionListener(this);
        deleteProductButton.setToolTipText("Enter product number first");
        window.add(deleteProductButton);

        // Spacer to force alignment
        window.add(new JLabel("                                                                       "));

        // Button to clear all the sales figures
        clearAllSalesButton = new JButton("Clear sales data");
        clearAllSalesButton.addActionListener(this);
        window.add(clearAllSalesButton);

        // Spacer to force new line
        window.add(new JLabel("                                                                       "));

        // Button to sort the sales table by name
        sortByNameButton = new JButton("Sort table by name");
        sortByNameButton.addActionListener(this);
        window.add(sortByNameButton);

        // Button to sort the sales table by sales descending
        sortBySalesDescendingButton = new JButton("Sort table by sales (descending)");
        sortBySalesDescendingButton.addActionListener(this);
        window.add(sortBySalesDescendingButton);

        // The drawing area for displaying all data and pie chart
        displayArea = new JPanel()
        {
            // paintComponent is called automatically when a screen refresh is needed
            public void paintComponent(Graphics g)
            {
                // g is a cleared panel area
                super.paintComponent(g); // Paint the panel's background
                paintScreen(g);          // Then the required graphics
            }
        };
        displayArea.setPreferredSize(new Dimension(DISPLAY_WIDTH, DISPLAY_HEIGHT));
        displayArea.setBackground(Color.white);
        window.add(displayArea);
    }

    /**
     * Event handler for button clicks.
     *
     * Select an appropriate action for the button clicked,
     * and re-display the table and pie chart
     */
    public void actionPerformed(ActionEvent event)
    {
        // Choose the appropriate response

        if (event.getSource() == newProductButton)
            addNewProductAction();

        if (event.getSource() == updateSalesButton)
            updateSalesAction();

        if (event.getSource() == deleteProductButton)
            deleteProductAction();

        if (event.getSource() == clearAllSalesButton)
            clearAllSales();

        if (event.getSource() == sortByNameButton)
            sortByName();

        if (event.getSource() == sortBySalesDescendingButton)
            sortBySalesDescending();

        // And refresh the display
        repaint();
    }

    /**
     * Action adding a new product with name from the entry text fields, with zero sales
     *
     * Only adds if the name field is not empty (other fields do not matter),
     * and if there is space in the arrays.
     * Pops up dialogue box giving reason if product is not added.
     */
    private void addNewProductAction()
    {
        // Fetch the new name and trim white space
        String newName = newProductNameEntryField.getText().trim();
        newProductNameEntryField.setText("");
        if (newName.length() == 0)            // Check and exit if the new name is empty
        {
            JOptionPane.showMessageDialog(null, "No name entered");
            // Return input focus to the product name text field
            newProductNameEntryField.requestFocusInWindow();
            return;
        }
        boolean ok = addNewProduct(newName);  // Try to add, ok reports the result
        if (!ok)                              // Check for failure
            JOptionPane.showMessageDialog(null, "No space for new product");
        // Return input focus to the product name text field
        newProductNameEntryField.requestFocusInWindow();
    }

    /**
     * Action updating a sales figure: the product number and new sales figures are fetched from text fields,
     * parsed, checked and action is taken.
     */
    private void updateSalesAction()
    {
        try
        {
            // Fetch the new sales details, trimming white space
            int productNumber = Integer.parseInt(productNumberEntryField.getText().trim());
            int newSales = Integer.parseInt(productSalesEntryField.getText().trim());
            // Check that the inputs are sensible: warning and ignore if not
            if (productNumber < 1 || productNumber > actualProducts || newSales < 0)
            {
                JOptionPane.showMessageDialog(this, "Input out of range - please correct");
                // Return input focus to the product number text field
                productNumberEntryField.requestFocusInWindow();
                return;
            }

            // Clear the inputs
            productNumberEntryField.setText("");
            productSalesEntryField.setText("");

            // Data OK, so update the sales tables
            updateSales(productNumber, newSales);
        }
        catch (NumberFormatException e)
        {
            // Invalid numerical input: warn and ignore
            JOptionPane.showMessageDialog(this, "Non-numerical input - please correct");
            // Return input focus to the product number text field
            productNumberEntryField.requestFocusInWindow();
            return;
        }
        // Return input focus to the product number text field
        productNumberEntryField.requestFocusInWindow();
    }

    /**
     * Action deleting a product: the product number is fetched from the text field,
     * parsed, checked and action is taken.
     */
    private void deleteProductAction()
    {
        try
        {
            // Fetch the product number to be deleted, trimming white space
            int productNumber = Integer.parseInt(productNumberEntryField.getText().trim());
            // Check that the input is sensible: warning and ignore if not
            if (productNumber < 1 || productNumber > actualProducts)
            {
                JOptionPane.showMessageDialog(this, "Input out of range - please correct");
                // Return input focus to the product number text field
                productNumberEntryField.requestFocusInWindow();
                return;
            }

            // Clear the input
            productNumberEntryField.setText("");

            // Data OK, so delete the product
            deleteProduct(productNumber);
        }
        catch (NumberFormatException e)
        {
            // Invalid numerical input: warn and ignore
            JOptionPane.showMessageDialog(this, "Non-numerical input - please correct");
            // Return input focus to the product number text field
            productNumberEntryField.requestFocusInWindow();
            return;
        }
        // Return input focus to the product number text field
        productNumberEntryField.requestFocusInWindow();
    }

   /**
     * Redraw all the sales data on the given graphics area
     */
    public void paintScreen(Graphics g)
    {
        drawSalesTable(g);
        drawPieChart(g);
    }

    /**
     * Draw a table of the sales data, with columns for the percentage sales.
     * Draws the whole table but only entries up to actualProducts.
     */
    private void drawSalesTable(Graphics g)
    {
        int y = TOP_Y;   // Start at top and step y in lines down the display

        //Heading
        g.setColor(Color.BLACK);
        g.setFont(TITLE_FONT);
        g.drawString("Product sales data:", LEFT_X, y);
        y = y + 2 * LINE_GAP;

        // Table column headers
        g.setFont(HEADING_FONT);
        g.drawString("No", PRODUCT_X, y);
        g.drawString("Name", NAME_X, y);
        g.drawString("Sales", SALES_X, y);
        g.drawString("%age", PERCENT_X, y);
        y = y + LINE_GAP;

        // The table of sales data, with colour key
        // Draw the whole table but only entries up to actualProducts
        g.setFont(NORMAL_FONT);
        g.drawLine(LEFT_X, y - LINE_GAP + 5, RIGHT_X, y - LINE_GAP + 5);  // Top box line
        for (int productNumber = 1; productNumber <= MAX_PRODUCTS; productNumber++)
        {
            g.setColor(Color.black);
            g.drawLine(LEFT_X, y - LINE_GAP + 5, LEFT_X, y + 5);          // Box left side
            g.drawLine(NAME_X - 5, y - LINE_GAP + 5, NAME_X - 5, y + 5);  // Box line between number and name
            g.drawLine(SALES_X - 5, y - LINE_GAP + 5, SALES_X - 5, y + 5);// Box line between name and sales
            g.drawLine(PERCENT_X - 5, y - LINE_GAP + 5, PERCENT_X - 5, y + 5);// Box line between sales and %age
            g.drawLine(RIGHT_X, y - LINE_GAP + 5, RIGHT_X, y + 5);        // Box right side
            g.drawLine(LEFT_X, y + 5, RIGHT_X, y + 5);                    // Box line below row

            g.drawString(Integer.toString(productNumber), PRODUCT_X, y);  // First column: product number
            if (productNumber <= actualProducts)                          // Only display details where there are products
            {
                g.drawString(productName[productNumber], NAME_X, y);          // Second column: product name
                g.drawString(Integer.toString(productSales[productNumber]), SALES_X, y); // Third column: sales figure
                g.drawString(String.format("%.1f", percentage[productNumber]), PERCENT_X, y); // Fourth column: %age of sales to 1 dec place
            }

            g.setColor(chartColour[productNumber]);                       // Draw colour key on right
            g.fillRect(RIGHT_X + 1, y - LINE_GAP + 5, KEYWIDTH, LINE_GAP+1);
            y = y + LINE_GAP;  // Adjust down for next line
        }
    }

    // Pie top position and dimensions
    // East-West and North-South dimensions of oval, and top-bottom thickness
    private final int pieX = 100, pieY = 300, pieWidthEW = 400,
    pieWidthNS = 250, pieThickness = 40;

    /**
     * Draw a piechart of the sales data, with products colour coded.
     * The percentage data gives the fraction of 360 degrees for the products.
     */
    private void drawPieChart(Graphics g)
    {
        // Draw the pie chart foundation
        g.setColor(Color.lightGray);        // Bottom
        g.fillOval(pieX,pieY + pieThickness,pieWidthEW,pieWidthNS);
        g.setColor(Color.black);
        g.drawOval(pieX,pieY + pieThickness,pieWidthEW,pieWidthNS);
        g.setColor(Color.lightGray);        // Sides of pie
        g.fillRect(pieX,pieY + pieWidthNS / 2,pieWidthEW,pieThickness);
        g.setColor(Color.black);
        g.drawLine(pieX,pieY + pieWidthNS / 2,pieX,
            pieY + pieWidthNS / 2 + pieThickness);
        g.drawLine(pieX + pieWidthEW,pieY + pieWidthNS / 2,pieX + pieWidthEW,
            pieY + pieWidthNS / 2 + pieThickness);
        g.setColor(Color.lightGray);  // Blank "top" of pie (the black edge is drawn after the wedges)
        g.fillOval(pieX,pieY,pieWidthEW,pieWidthNS);

        // Draw the coloured wedges
        if (totalSales != 0) {
            // If the total is not zero, calculate the wedge sizes and draw the wedges.
            // [If total is zero, the percentages are not useful for calculating wedge sizes, 
            // and the pie is left blank.]
            // Angles in degrees, calculated in float and rounded
            // Note: the wedge sizes may slightly over or under fill the 360 degrees
            // due to rounding, but it's OK to ignore the small error

            // Draw one wedge for each product, anticlockwise from East
            // percentage array gives the proportion, chartColour gives the wedge colour
            float wedgeStartAngle = 0;
            for (int productNumber = 1; productNumber <= actualProducts; productNumber++)
            {
                // Calculate angle for this product wedge
                float thisProductAngle = 360f * percentage[productNumber]/100;
                // Calculate this wedge angle as accurately as possible, given rounding
                int wedgeAngle = Math.round(wedgeStartAngle + thisProductAngle) - Math.round(wedgeStartAngle);
                // And draw the wedge
                g.setColor(chartColour[productNumber]);
                g.fillArc(pieX,pieY,pieWidthEW,pieWidthNS,Math.round(wedgeStartAngle),wedgeAngle);
                // Adjust for next wedge
                wedgeStartAngle = wedgeStartAngle + thisProductAngle;
            }
        }

        // Finally the black edge of the "top" of the pie
        g.setColor(Color.black);
        g.drawOval(pieX,pieY,pieWidthEW,pieWidthNS);

    }

    //////////////////// DO NOT ALTER THE CODE ABOVE THIS LINE ////////////////////////////////
    //////////////////// COMPLETE THE METHOD BODIES BELOW THIS LINE ///////////////////////////

    /**
     * The maximum permitted number of products
     */
    private final int MAX_PRODUCTS = 10;

    /**
     * This gives the actual number of products held in the elements of the arrays below.
     * They are held in the elements indexed 1 to actualProducts.
     * Initially there will be no products.
     */
    private int actualProducts;

    /**
     * These arrays hold all the products and sales data:
     * The product number (from 1 to actualProducts) is the index in the arrays
     * where the product's data is held.
     * Sales figures are counted quantities, so int.
     * The percentages are held as floats, and displayed to 1 decimal place.
     * Element 0 is unused, so array sizes are MAX_PRODUCTS+1.
     */
    private String[] productName;  // The name of each product
    private int[] productSales;    // The number of sales of each product
    private float[] percentage;    // The proportion of total sales for each product

    /**
     * This always holds the current total sales
     */
    private int totalSales;

    /**
     * This method creates the product arrays: empty as they have no actual products yet.
     * Initialize total sales to 0.
     *
     * For convenience, the chart colours are also set up here.
     *
     * THIS METHOD DOES NOT NEED ALTERING, except to delete the dummy data
     * once the application is working.
     */
    private void initializeSalesData()
    {
        productName = new String[MAX_PRODUCTS+1];
        productSales = new int[MAX_PRODUCTS+1];
        percentage = new float[MAX_PRODUCTS+1];

        actualProducts = 0;
        totalSales = 0;

        Color[] colours = {null, Color.pink, Color.magenta, Color.red, Color.orange, Color.yellow,
                Color.green, Color.cyan, Color.blue, Color.gray, Color.black};
        chartColour = colours;

        // Some dummy data for early testing - delete these four lines after early testing
        productName[1] = "Bread"; productSales[1] = 23; percentage[1] = 33;
        productName[2] = "Milk";  productSales[2] = 46; percentage[2] = 67;
        totalSales = 69;
        actualProducts = 2;
    }

    /**
     * This method clears the total sales, sales figures and percentage arrays to
     * all zeros.
     * Note that the current set of product names is kept.
     */
    private void clearAllSales()
    {
        // TO BE DONE: Implement this method body - see the comment above
    }

    /**
     * This method re-computes the percentage of total sales for each product:
     * The percentage array is refilled with newly calculated values.
     * The percentage value for each product is 100 * sales for the product / total sales,
     * calculated as a float.
     * Note: If totalSales is 0 then it makes no sense to compute percentages
     * so they are each set to 0 as a "safe" value.
     */
    private void computePercentages()
    {
        // TO BE DONE: Implement this method body - see the comment above
    }

    /**
     * This method adds a new product to the database in the next available location,
     * if there is space.
     * Result is true if the product was added, or false if there was no space.
     *
     * @param newName   The new product name
     * @return          A boolean indicating successful, or not
     */
    private boolean addNewProduct(String newName)
    {
        // TO BE DONE: Need to check if there is space available, and return false if not.

        actualProducts++;                      // Count one more product
        productName[actualProducts] = newName; // Add name at first free element the array
        productSales[actualProducts] = 0;      // Zero the corresponding initial sales data
        percentage[actualProducts] = 0;
        // Note: Other products' percentages do not change
        return true;                           // Success
    }

    /**
     * This method <b>replaces</b> one product sales figure.
     * Assumes that productNumber and productSales are sensible.
     * Adjusts the running total sales figure, and all products' percentages of sales.
     *
     * @param productNumber   the product number for the new sales figure - the index in the data arrays
     * @param prodSales       the new sales of the product
     */
    public void updateSales(int productNumber, int prodSales)
    {
        // TO BE DONE: Implement this method body - see the comment above
    }

    /**
     * This method deletes one product and its sales figure.
     * Assumes that deleteNumber is sensible.
     * Adjusts the running total sales figure, and products' percentages of sales.
     *
     * @param deleteNumber   the product number for the product - the index in the data arrays
     */
    private void deleteProduct(int deleteNumber)
    {
        // TO BE DONE: Implement this method body - see the comment above
    }

    /**
     * This method re-orders the products in the database so that the
     * names are in ascending case-insensitive alphabetic order.
     * Of course, the sales and percentages are rearranged as well!
     */
    private void sortByName()
    {
        // TO BE DONE: Implement this method body - see the comment above
    }

    /**
     * This method re-orders the products in the database so that the
     * sales figures are in descending order, that is with the highest sales first.
     * Of course, the names and percentages are rearranged as well!
     */
    private void sortBySalesDescending()
    {
        // TO BE DONE: Implement this method body - see the comment above
    }

} // End of program
