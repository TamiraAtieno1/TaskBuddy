/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package taskbuddy;

// Main.java (Console Application)
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TaskBuddy {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            TaskManager taskManager = new TaskManager();

            // Categories now use String IDs
            Category workCategory = new Category(taskManager.generateCategoryId(), "Work");
            Category personalCategory = new Category(taskManager.generateCategoryId(), "Personal");
            Category studiesCategory = new Category(taskManager.generateCategoryId(), "Studies");

            System.out.println("Welcome to the Interactive To-Do List Application!");

            int choice;
            do {
                displayMenu();
                System.out.print("Enter your choice: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1 -> addTask(scanner, taskManager, workCategory, personalCategory, studiesCategory);
                        case 2 -> viewAllTasks(taskManager);
                        case 3 -> updateTask(scanner, taskManager, workCategory, personalCategory, studiesCategory);
                        case 4 -> deleteTask(scanner, taskManager);
                        case 5 -> filterTasks(scanner, taskManager, workCategory, personalCategory, studiesCategory);
                        case 6 -> sortTasks(scanner, taskManager);
                        case 7 -> markTaskCompleted(scanner, taskManager);
                        case 8 -> viewCompletedTasks(taskManager);
                        case 9 -> viewIncompleteTasks(taskManager);
                        case 0 -> System.out.println("Exiting application. Goodbye!");
                        default -> System.out.println("Invalid choice. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Consume the invalid input
                    choice = -1; // Set choice to an invalid value to re-display menu
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                    e.printStackTrace(); // Print stack trace for debugging
                    choice = -1;
                }
                System.out.println("\n-----------------------------------\n");
            } while (choice != 0);
        }
    }

    // Display the main menu options
    private static void displayMenu() {
        System.out.println("--- To-Do List Menu ---");
        System.out.println("1. Add New Task");
        System.out.println("2. View All Tasks");
        System.out.println("3. Update Task");
        System.out.println("4. Delete Task");
        System.out.println("5. Filter Tasks (by Category or Status)");
        System.out.println("6. Sort Tasks");
        System.out.println("7. Mark Task as Completed");
        System.out.println("8. View Completed Tasks");
        System.out.println("9. View Incomplete Tasks");
        System.out.println("0. Exit");
    }

    // addTask method
    private static void addTask(Scanner scanner, TaskManager taskManager, Category work, Category personal, Category studies) {
        System.out.println("\n--- Add New Task ---");
        System.out.print("Enter Task Title: ");
        String title = scanner.nextLine();

        System.out.print("Enter Task Description (optional): ");
        String description = scanner.nextLine();

        LocalDate dueDate = null;
        boolean validDate = false;
        while (!validDate) {
            System.out.print("Enter Due Date (YYYY-MM-DD): ");
            String dateString = scanner.nextLine();
            try {
                dueDate = LocalDate.parse(dateString);
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        System.out.print("Enter Priority (High, Medium, Low): ");
        String priority = scanner.nextLine();

        System.out.print("Enter Status (To Do, In Progress, Completed): ");
        String status = scanner.nextLine();

        // Category selection logic
        Category selectedCategory = null;
        boolean validCategory = false;
        while (!validCategory) {
            System.out.println("Select Category (1: Work, 2: Personal, 3: Studies): ");
            try {
                int categoryChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                switch (categoryChoice) {
                    case 1 -> {
                        selectedCategory = work; validCategory = true;
                    }
                    case 2 -> {
                        selectedCategory = personal; validCategory = true;
                    }
                    case 3 -> {
                        selectedCategory = studies; validCategory = true;
                    }
                    default -> System.out.println("Invalid category choice. Please enter 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number for category choice.");
                scanner.nextLine(); // Consume the invalid input
            }
        }

        taskManager.addTask(title, description, dueDate, priority, status, selectedCategory);
    }

    // viewAllTasks method - now calls listAllTasks()
    private static void viewAllTasks(TaskManager taskManager) {
        System.out.println("\n--- All Tasks ---");
        List<Task> tasks = taskManager.listAllTasks(); // Changed to listAllTasks()
        if (tasks.isEmpty()) {
            System.out.println("No tasks to display.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    // updateTask method - now uses String taskId and .equals()
    private static void updateTask(Scanner scanner, TaskManager taskManager, Category work, Category personal, Category studies) {
        System.out.println("\n--- Update Task ---");
        System.out.print("Enter Task ID to Update: ");
        try {
            String taskId = scanner.nextLine(); // Read taskId as String

            // Use listAllTasks() and .equals() for comparison
            Task existingTask = taskManager.listAllTasks().stream()
                                     .filter(t -> t.getTaskId().equals(taskId))
                                     .findFirst()
                                     .orElse(null);

            if (existingTask == null) {
                System.out.println("Task with ID " + taskId + " not found.");
                return;
            }

            System.out.println("Updating Task ID: " + taskId + " (Current Title: " + existingTask.getTitle() + ")");
            System.out.print("Enter New Task Title (or press Enter to keep '" + existingTask.getTitle() + "'): ");
            String title = scanner.nextLine();
            if (title.isEmpty()) title = existingTask.getTitle();

            System.out.print("Enter New Task Description (or press Enter to keep '" + existingTask.getDescription() + "'): ");
            String description = scanner.nextLine();
            if (description.isEmpty()) description = existingTask.getDescription();

            LocalDate dueDate = existingTask.getDueDate();
            boolean validDate = false;
            while (!validDate) {
                System.out.print("Enter New Due Date (YYYY-MM-DD, or press Enter to keep '" + existingTask.getDueDate() + "'): ");
                String dateString = scanner.nextLine();
                if (dateString.isEmpty()) {
                    validDate = true;
                } else {
                    try {
                        dueDate = LocalDate.parse(dateString);
                        validDate = true;
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date format. Please use YYYY-MM-DD.");
                    }
                }
            }

            System.out.print("Enter New Priority (High, Medium, Low, or press Enter to keep '" + existingTask.getPriority() + "'): ");
            String priority = scanner.nextLine();
            if (priority.isEmpty()) priority = existingTask.getPriority();

            System.out.print("Enter New Status (To Do, In Progress, Completed, or press Enter to keep '" + existingTask.getStatus() + "'): ");
            String status = scanner.nextLine();
            if (status.isEmpty()) status = existingTask.getStatus();

            // Category selection logic re-added
            Category selectedCategory = existingTask.getCategory();
            boolean validCategory = false;
            while (!validCategory) {
                System.out.println("Select New Category (1: Work, 2: Personal, 3: Studies, or 0 to keep current '" + (existingTask.getCategory() != null ? existingTask.getCategory().getCategoryName() : "None") + "'): ");
                try {
                    int categoryChoice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    switch (categoryChoice) {
                        case 0: validCategory = true; break;
                        case 1: selectedCategory = work; validCategory = true; break;
                        case 2: selectedCategory = personal; validCategory = true; break;
                        case 3: selectedCategory = studies; validCategory = true; break;
                        default: System.out.println("Invalid category choice. Please enter 0, 1, 2, or 3.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number for category choice.");
                    scanner.nextLine(); // Consume the invalid input
                }
            }

            taskManager.updateTask(taskId, title, description, dueDate, priority, status, selectedCategory);

        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid Task ID (String).");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // deleteTask method - now uses String taskId
    private static void deleteTask(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- Delete Task ---");
        System.out.print("Enter Task ID to Delete: ");
        try {
            String taskId = scanner.nextLine(); // Read taskId as String
            taskManager.removeTask(taskId);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid Task ID (String).");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // filterTasks method
    private static void filterTasks(Scanner scanner, TaskManager taskManager, Category work, Category personal, Category studies) {
        System.out.println("\n--- Filter Tasks ---");
        System.out.println("1. Filter by Category");
        System.out.println("2. Filter by Status");
        System.out.print("Enter filter option: ");
        try {
            int filterChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            List<Task> filteredTasks = null;
            if (filterChoice == 1) {
                Category selectedCategory = null;
                boolean validCategory = false;
                while (!validCategory) {
                    System.out.println("Select Category to filter by (1: Work, 2: Personal, 3: Studies): ");
                    try {
                        int categoryChoice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        switch (categoryChoice) {
                            case 1: selectedCategory = work; validCategory = true; break;
                            case 2: selectedCategory = personal; validCategory = true; break;
                            case 3: selectedCategory = studies; validCategory = true; break;
                            default: System.out.println("Invalid category choice. Please enter 1, 2, or 3.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a number for category choice.");
                        scanner.nextLine(); // Consume the invalid input
                    }
                }
                if (selectedCategory != null) {
                    filteredTasks = taskManager.filterTasksByCategory(selectedCategory);
                }
            } else if (filterChoice == 2) {
                System.out.print("Enter Status to filter by (e.g., To Do, In Progress, Completed): ");
                String status = scanner.nextLine();
                filteredTasks = taskManager.filterTasksByStatus(status);
            } else {
                System.out.println("Invalid filter option.");
                return;
            }

            if (filteredTasks != null && !filteredTasks.isEmpty()) {
                System.out.println("\nFiltered Tasks:");
                filteredTasks.forEach(System.out::println);
            } else {
                System.out.println("No tasks found matching your filter criteria.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // Allows sorting tasks either by due date or priority.
    private static void sortTasks(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- Sort Tasks ---");
        System.out.println("1. Sort by Due Date");
        System.out.println("2. Sort by Priority");
        System.out.print("Enter sort option: ");
        try {
            int sortChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            List<Task> sortedTasks = null;
            if (sortChoice == 1) {
                sortedTasks = taskManager.sortTasksByDueDate();
            } else if (sortChoice == 2) {
                sortedTasks = taskManager.sortTasksByPriority();
            } else {
                System.out.println("Invalid sort option.");
                return;
            }

            if (sortedTasks != null && !sortedTasks.isEmpty()) {
                System.out.println("\nSorted Tasks:");
                sortedTasks.forEach(System.out::println);
            } else {
                System.out.println("No tasks to display for sorting.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // Handles marking a specific task as completed. - now uses String taskId
    private static void markTaskCompleted(Scanner scanner, TaskManager taskManager) {
        System.out.println("\n--- Mark Task as Completed ---");
        System.out.print("Enter Task ID to mark as Completed: ");
        try {
            String taskId = scanner.nextLine(); // Read taskId as String
            taskManager.markTaskAsCompleted(taskId);
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid Task ID (String).");
            scanner.nextLine(); // Consume the invalid input
        }
    }

    // Displays all tasks that have been marked as "Completed".
    private static void viewCompletedTasks(TaskManager taskManager) {
        System.out.println("\n--- Completed Tasks ---");
        List<Task> tasks = taskManager.getCompletedTasks();
        if (tasks.isEmpty()) {
            System.out.println("No completed tasks to display.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    // Displays all tasks that are NOT marked as "Completed".
    private static void viewIncompleteTasks(TaskManager taskManager) {
        System.out.println("\n--- Incomplete Tasks ---");
        List<Task> tasks = taskManager.getIncompleteTasks();
        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks to display.");
        } else {
            tasks.forEach(System.out::println);
        }
    }
}
