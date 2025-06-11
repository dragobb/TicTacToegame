/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package bonifacio_bscs1a.tictactoe;

/**
 *
 * @author ariel
 */

import javax.swing.*;
import java.awt.*;

public class TicTacToe extends JFrame {
    private final JButton[][] buttons = new JButton[3][3];
    private boolean xTurn = true;
    private JLabel statusLabel;
    private JButton restartButton, homeButton;
    private final CardLayout cardLayout;
    private final JPanel mainContainer;
    private Timer flashTimer;
    private boolean flashState = false;
    private JLabel winnerLabel;
    private JButton[] winningButtons = new JButton[3]; 
    
    private static final String LOADING_SCREEN = "loading";
    private static final String HOME_SCREEN = "home";
    private static final String GAME_SCREEN = "game";

    public TicTacToe() {
        setTitle("Tic-Tac-Toe");
        setSize(420, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        getContentPane().setBackground(new Color(20, 20, 30));
        
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(new Color(20, 20, 30));
        
        createLoadingScreen();
        createHomeScreen();
        createGameScreen();
        
        add(mainContainer);
        
        cardLayout.show(mainContainer, LOADING_SCREEN);
        startLoadingAnimation();
    }

    private void createLoadingScreen() {
        JPanel loadingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 30, 50),
                    0, getHeight(), new Color(60, 30, 80)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        loadingPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 50, 0));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setBackground(new Color(40, 40, 60));
        progressBar.setForeground(new Color(100, 200, 255));
        progressBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 50, 0, 50),
            BorderFactory.createLineBorder(new Color(60, 60, 80), 1)
        ));
        progressBar.setPreferredSize(new Dimension(300, 12));
        progressBar.setBorderPainted(false);
        
        JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        loadingLabel.setForeground(new Color(200, 200, 200));
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 100, 0));
        
        loadingPanel.add(titleLabel, BorderLayout.NORTH);
        loadingPanel.add(progressBar, BorderLayout.CENTER);
        loadingPanel.add(loadingLabel, BorderLayout.SOUTH);
        
        mainContainer.add(loadingPanel, LOADING_SCREEN);
    }

    private void createHomeScreen() {
        JPanel homePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 25, 40),
                    0, getHeight(), new Color(50, 25, 70)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        homePanel.setLayout(new BorderLayout());
        
        JLabel homeTitle = new JLabel("Tic Tac Toe", SwingConstants.CENTER);
        homeTitle.setFont(new Font("Segoe UI", Font.BOLD, 42));
        homeTitle.setForeground(Color.WHITE);
        homeTitle.setBorder(BorderFactory.createEmptyBorder(80, 0, 60, 0));
        
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        
        JButton playButton = createModernButton("PLAY", new Color(50, 150, 250));
        JButton aboutButton = createModernButton("ABOUT", new Color(100, 100, 100));
        JButton exitButton = createModernButton("EXIT", new Color(200, 50, 50));
        
        playButton.addActionListener(e -> {
            cardLayout.show(mainContainer, GAME_SCREEN);
            resetGame();
        });
        
        aboutButton.addActionListener(e -> showAboutDialog());
        exitButton.addActionListener(e -> System.exit(0));
        
        gbc.gridy = 0;
        buttonsPanel.add(playButton, gbc);
        gbc.gridy = 1;
        buttonsPanel.add(aboutButton, gbc);
        gbc.gridy = 2;
        buttonsPanel.add(exitButton, gbc);
        
        homePanel.add(homeTitle, BorderLayout.NORTH);
        homePanel.add(buttonsPanel, BorderLayout.CENTER);
        
        mainContainer.add(homePanel, HOME_SCREEN);
    }

    private void createGameScreen() {
        JPanel gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 20, 35),
                    0, getHeight(), new Color(40, 20, 60)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel gameTitle = new JLabel("TIC-TAC-TOE", SwingConstants.CENTER);
        gameTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        gameTitle.setForeground(Color.WHITE);
        winnerLabel = gameTitle;
        
       
        JPanel gridContainer = new JPanel(new BorderLayout());
        gridContainer.setOpaque(false);
        
        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 8, 8));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 36);
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton btn = createGameButton();
                btn.setFont(buttonFont);
                final int r = row, c = col;
                btn.addActionListener(e -> handleClick(btn, r, c));
                buttons[row][col] = btn;
                gridPanel.add(btn);
            }
        }
        
        gridContainer.add(gridPanel, BorderLayout.CENTER);
        
       
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        statusLabel.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        restartButton = createModernButton("RESTART", new Color(50, 150, 250));
        restartButton.setPreferredSize(new Dimension(100, 35));
        restartButton.addActionListener(e -> resetGame());
        
        homeButton = createModernButton("HOME", new Color(100, 100, 100));
        homeButton.setPreferredSize(new Dimension(80, 35));
        homeButton.addActionListener(e -> cardLayout.show(mainContainer, HOME_SCREEN));
        
        buttonPanel.add(restartButton);
        buttonPanel.add(homeButton);
        
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        gamePanel.add(gameTitle, BorderLayout.NORTH);
        gamePanel.add(gridContainer, BorderLayout.CENTER);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainContainer.add(gamePanel, GAME_SCREEN);
    }

    private JButton createModernButton(String text, Color baseColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color color = getModel().isPressed() ? baseColor.darker() : 
                             getModel().isRollover() ? baseColor.brighter() : baseColor;
                
                g2d.setColor(color);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
                
                g2d.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(180, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private JButton createGameButton() {
        JButton button = new JButton("") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                
                boolean isWinning = false;
                boolean isBlurred = false;
                
                if (winningButtons[0] != null) {
                    for (JButton winBtn : winningButtons) {
                        if (winBtn == this) {
                            isWinning = true;
                            break;
                        }
                    }
                    if (!isWinning && !getText().isEmpty()) {
                        isBlurred = true;
                    }
                }
                
                
                Color bgColor;
                if (isWinning) {
                    bgColor = new Color(255, 215, 0, 100);
                } else if (isBlurred) {
                    bgColor = new Color(60, 60, 100, 80); 
                } else {
                    bgColor = getModel().isRollover() && isEnabled() ? 
                        new Color(80, 80, 120, 180) : new Color(60, 60, 100, 150);
                }
                
                g2d.setColor(bgColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                
                Color borderColor = isWinning ? new Color(255, 215, 0, 255) : new Color(100, 100, 150, 200);
                int borderWidth = isWinning ? 3 : 2;
                g2d.setColor(borderColor);
                g2d.setStroke(new BasicStroke(borderWidth));
                g2d.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 12, 12);
                
                
                if (!getText().isEmpty()) {
                    Color textColor = getForeground();
                    if (isBlurred) {
                        
                        textColor = new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue(), 100);
                    }
                    g2d.setColor(textColor);
                    g2d.setFont(getFont());
                    FontMetrics fm = g2d.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(getText())) / 2;
                    int y = (getHeight() + fm.getAscent()) / 2 - 2;
                    g2d.drawString(getText(), x, y);
                }
                
                g2d.dispose();
            }
        };
        
        button.setPreferredSize(new Dimension(80, 80));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private void startLoadingAnimation() {
        Timer loadingTimer = new Timer(2500, e -> {
            cardLayout.show(mainContainer, HOME_SCREEN);
            ((Timer) e.getSource()).stop();
        });
        loadingTimer.setRepeats(false);
        loadingTimer.start();
    }

    private void showAboutDialog() {
        JDialog dialog = new JDialog(this, "About", true);
        dialog.setSize(300, 250);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(30, 30, 50));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 50));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel aboutText = new JLabel("<html><div style='text-align: center;'>" +
            "<h2 style='color: white;'>ComProg2 Tic-Tac-Toe</h2>" +
            "<p style='color: #cccccc;'>Version 1.o</p>" +
            "<p style='color: #cccccc;'>Created by Ariel Bonifacio</p>" +
            "<p style='color: #cccccc;'>Created by Jonel Arcena</p>" +
            "<p style='color: #cccccc;'>Created by Edrian Bencito</p>" +
            "<p style='color: #cccccc;'>Created by Jan Andrei Lagare</p>" +
            "<p style='color: #cccccc;'>Created for Laboratory Activity 7</p>" +
            "</div></html>");
        
        JButton okButton = createModernButton("OK", new Color(50, 150, 250));
        okButton.setPreferredSize(new Dimension(80, 30));
        okButton.addActionListener(e -> dialog.dispose());
        
        panel.add(aboutText, BorderLayout.CENTER);
        panel.add(okButton, BorderLayout.SOUTH);
        
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void handleClick(JButton btn, int row, int col) {
        if (!btn.getText().equals("") || winningButtons[0] != null) return;

        btn.setText(xTurn ? "X" : "O");
        btn.setForeground(xTurn ? new Color(100, 200, 255) : new Color(255, 100, 100));

        if (checkWin(row, col)) {
            String winner = xTurn ? "X" : "O";
            statusLabel.setText("Player " + winner + " Wins!");
            winnerLabel.setText(" PLAYER " + winner + " WINS! ");
            winnerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            winnerLabel.setForeground(new Color(255, 215, 0));
            startFlashingAnimation();
            disableButtons();
            repaint(); 
        } else if (isDraw()) {
            statusLabel.setText("It's a Draw!");
            winnerLabel.setText(" IT'S A DRAW! ");
            winnerLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            winnerLabel.setForeground(new Color(255, 215, 0));
            startFlashingAnimation();
        } else {
            xTurn = !xTurn;
            statusLabel.setText("Player " + (xTurn ? "X" : "O") + "'s Turn");
        }
    }

    private boolean checkWin(int lastRow, int lastCol) {
        
        if (checkLine(buttons[lastRow][0], buttons[lastRow][1], buttons[lastRow][2])) {
            setWinningButtons(buttons[lastRow][0], buttons[lastRow][1], buttons[lastRow][2]);
            return true;
        }
        
        
        if (checkLine(buttons[0][lastCol], buttons[1][lastCol], buttons[2][lastCol])) {
            setWinningButtons(buttons[0][lastCol], buttons[1][lastCol], buttons[2][lastCol]);
            return true;
        }
        
        
        if (lastRow == lastCol && checkLine(buttons[0][0], buttons[1][1], buttons[2][2])) {
            setWinningButtons(buttons[0][0], buttons[1][1], buttons[2][2]);
            return true;
        }
        
        if (lastRow + lastCol == 2 && checkLine(buttons[0][2], buttons[1][1], buttons[2][0])) {
            setWinningButtons(buttons[0][2], buttons[1][1], buttons[2][0]);
            return true;
        }
        
        return false;
    }

    private boolean checkLine(JButton b1, JButton b2, JButton b3) {
        return !b1.getText().equals("") &&
               b1.getText().equals(b2.getText()) &&
               b2.getText().equals(b3.getText());
    }

    private void setWinningButtons(JButton btn1, JButton btn2, JButton btn3) {
        winningButtons[0] = btn1;
        winningButtons[1] = btn2;
        winningButtons[2] = btn3;
    }

    private void startFlashingAnimation() {
        flashTimer = new Timer(600, e -> {
            flashState = !flashState;
            if (flashState) {
                winnerLabel.setForeground(new Color(255, 215, 0)); 
            } else {
                winnerLabel.setForeground(new Color(255, 100, 100)); 
            }
        });
        flashTimer.start();
    }

    private boolean isDraw() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                if (btn.getText().equals("")) return false;
        return true;
    }

    private void disableButtons() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);
    }

    private void resetGame() {
        if (flashTimer != null && flashTimer.isRunning()) {
            flashTimer.stop();
        }
        
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
                btn.setForeground(Color.WHITE);
            }
        }
        
        xTurn = true;
        statusLabel.setText("Player X's Turn");
        winnerLabel.setText("TIC-TAC-TOE");
        winnerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        winnerLabel.setForeground(Color.WHITE);
        winningButtons = new JButton[3]; 
        repaint();
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TicTacToe frame = new TicTacToe();
            frame.setVisible(true);
        });
    }
}