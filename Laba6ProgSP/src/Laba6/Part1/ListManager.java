package Laba6.Part1;

import java.awt.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.util.ArrayList;


public class ListManager extends Frame {
    
    List list1, list2, list3;
    TextField inputField;
    Button addButton, editButton, deleteButton;
    
    public ListManager() {
        setTitle("List Manager");
        setSize(600, 400);
        setLayout(new GridLayout(2, 3));

        list1 = new List();
        list2 = new List();
        list3 = new List();
        
        list1.add("Item A1");
        list1.add("Item A2");
        
        list2.add("Item B1");
        
        list3.add("Item C1");
        list3.add("Item C2");

        setupDragAndDrop(list1, list2, list3);
        setupDragAndDrop(list2, list3, list1);
        setupDragAndDrop(list3, list1, list2);

        add(list1);
        add(list2);
        add(list3);

        Panel controlPanel = new Panel();
        controlPanel.setLayout(new FlowLayout());

        inputField = new TextField(10);
        addButton = new Button("Add");
        editButton = new Button("Edit");
        deleteButton = new Button("Delete");

        controlPanel.add(inputField);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

        addButton.addActionListener(e -> addItemToList());
        editButton.addActionListener(e -> editSelectedItem());
        deleteButton.addActionListener(e -> deleteSelectedItem());

        add(new Label());
        add(controlPanel);
        add(new Label());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }

            public void windowIconified(WindowEvent e) {
                System.out.println("Window minimized");
            }

            public void windowDeiconified(WindowEvent e) {
                System.out.println("Window restored");
            }
        });

        setVisible(true);
    }

    private void setupDragAndDrop(List source, List target, List nextTarget) {
        DragSource ds = new DragSource();
        ds.createDefaultDragGestureRecognizer(source, DnDConstants.ACTION_MOVE, new DragGestureListener() {
            public void dragGestureRecognized(DragGestureEvent event) {
                String selectedItem = source.getSelectedItem();
                if (selectedItem != null) {
                    StringSelection text = new StringSelection(selectedItem);
                    ds.startDrag(event, DragSource.DefaultMoveDrop, text, new DragSourceListener() {
                        public void dragDropEnd(DragSourceDropEvent event) {
                            if (event.getDropSuccess()) {
                                source.remove(selectedItem);
                            }
                        }
                        public void dragEnter(DragSourceDragEvent event) {}
                        public void dragOver(DragSourceDragEvent event) {}
                        public void dropActionChanged(DragSourceDragEvent event) {}
                        public void dragExit(DragSourceEvent event) {}
                    });
                }
            }
        });

        DropTarget dt = new DropTarget(target, new DropTargetListener() {
            public void dragEnter(DropTargetDragEvent event) {
                event.acceptDrag(DnDConstants.ACTION_MOVE);
            }

            public void dragOver(DropTargetDragEvent event) {}

            public void drop(DropTargetDropEvent event) {
                try {
                    Transferable tr = event.getTransferable();
                    if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        event.acceptDrop(DnDConstants.ACTION_MOVE);
                        String draggedItem = (String) tr.getTransferData(DataFlavor.stringFlavor);
                        target.add(draggedItem);
                        event.dropComplete(true);
                    } else {
                        event.rejectDrop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    event.rejectDrop();
                }
            }

            public void dragExit(DropTargetEvent event) {}
            public void dropActionChanged(DropTargetDragEvent event) {}
        });
    }

    private void addItemToList() {
        String newItem = inputField.getText();
        if (!newItem.isEmpty()) {
            list2.add(newItem);
            inputField.setText("");
        }
    }

    private void editSelectedItem() {
        String selectedItem = list2.getSelectedItem();
        if (selectedItem != null) {
            String newItem = inputField.getText();
            if (!newItem.isEmpty()) {
                list2.replaceItem(newItem, list2.getSelectedIndex());
                inputField.setText("");
            }
        }
    }

    private void deleteSelectedItem() {
        String selectedItem = list2.getSelectedItem();
        if (selectedItem != null) {
            list2.remove(selectedItem);
        }
    }

    public static void main(String[] args) {
        new ListManager();
    }
}

