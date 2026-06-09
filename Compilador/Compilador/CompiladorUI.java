

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
//import java.awt.image.BufferedImage;
import java.io.*;

public class CompiladorUI extends JFrame {
    private final JTextArea editor;
    private final JTextArea mensagens;
    private final JLabel statusLabel;
    private File arquivoAtual;

    public CompiladorUI() {
        super("Compilador - Interface");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 800);
        setResizable(false);
        setLocationRelativeTo(null);

        JToolBar toolbar = criarBarraFerramentas();

        editor = new JTextArea();
        editor.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        editor.setBorder(new NumberedBorder());
        editor.setLineWrap(false);
        JScrollPane spEditor = new JScrollPane(
                editor,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        mensagens = new JTextArea();
        mensagens.setEditable(false);
        mensagens.setLineWrap(false);
        mensagens.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane spMsg = new JScrollPane(
                mensagens,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, spEditor, spMsg);
        split.setResizeWeight(0.7);
        split.setDividerSize(8);

        JPanel status = new JPanel(new BorderLayout());
        status.setPreferredSize(new Dimension(0, 25));
        statusLabel = new JLabel(" ");
        status.add(statusLabel, BorderLayout.WEST);
        status.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(toolbar, BorderLayout.NORTH);
        getContentPane().add(split, BorderLayout.CENTER);
        getContentPane().add(status, BorderLayout.SOUTH);

        configurarAtalhosGlobais();
    }

    private JToolBar criarBarraFerramentas() {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);
        tb.setPreferredSize(new Dimension(0, 70));
        tb.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        Dimension btnSize = new Dimension(160, 60);

        Action novo = new AbstractAction("novo [ctrl-n]", carregarIcone("/icons/newsfile.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { acaoNovo(); }
        };
        Action abrir = new AbstractAction("abrir [ctrl-o]", carregarIcone("/icons/openfolder.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { acaoAbrir(); }
        };
        Action salvar = new AbstractAction("salvar [ctrl-s]", carregarIcone("/icons/savefile.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { acaoSalvar(false); }
        };
        Action copiar = new AbstractAction("copiar [ctrl-c]", carregarIcone("/icons/copy.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { editor.copy(); }
        };
        Action colar = new AbstractAction("colar [ctrl-v]", carregarIcone("/icons/paste.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { editor.paste(); }
        };
        Action recortar = new AbstractAction("recortar [ctrl-x]", carregarIcone("/icons/cut.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { editor.cut(); }
        };
        Action compilar = new AbstractAction("compilar [F7]", carregarIcone("/icons/compilation.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { acaoCompilar(); }
        };
        Action equipe = new AbstractAction("equipe [F1]", carregarIcone("/icons/team.png")) {
            @Override
            public void actionPerformed(ActionEvent e) { mostrarMensagemUnica("Equipe: Gustavo Luchini, Emanuel Sergio Girardi, Gabriel Tormena "); }
        };

        criarBotao(tb, novo, btnSize);
        criarBotao(tb, abrir, btnSize);
        criarBotao(tb, salvar, btnSize);

        tb.addSeparator(new Dimension(8, 0));
        criarBotao(tb, copiar, btnSize);
        criarBotao(tb, colar, btnSize);
        criarBotao(tb, recortar, btnSize);

        tb.addSeparator(new Dimension(8, 0));
        criarBotao(tb, compilar, btnSize);

        tb.addSeparator(new Dimension(8, 0));
        criarBotao(tb, equipe, btnSize);

        return tb;
    }

    private JButton criarBotao(JToolBar tb, Action action, Dimension size) {
        JButton btn = new JButton(action);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFocusPainted(false);
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        btn.setMinimumSize(size);
        btn.setFocusable(false);
        btn.setIconTextGap(10);
        tb.add(btn);
        return btn;
    }

private Icon carregarIcone(String caminho) {
    return new ImageIcon(getClass().getResource(caminho));
}

/*
    private Icon criarIcone(Color color, String text) {
        int w = 48, h = 48;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillRoundRect(0, 0, w - 1, h - 1, 10, 10);
        g.setColor(Color.WHITE);
        Font f = new Font("SansSerif", Font.BOLD, text.length() > 2 ? 16 : 22);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int tx = (w - fm.stringWidth(text)) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, tx, ty);
        g.dispose();
        return new ImageIcon(img);
    }
   */  

    private void configurarAtalhosGlobais() {
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK), "novo");
        am.put("novo", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoNovo(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK), "abrir");
        am.put("abrir", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoAbrir(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "salvar");
        am.put("salvar", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoSalvar(false); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0), "compilar");
        am.put("compilar", new AbstractAction() { public void actionPerformed(ActionEvent e) { acaoCompilar(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "equipe");
        am.put("equipe", new AbstractAction() { public void actionPerformed(ActionEvent e) { mostrarMensagemUnica("Equipe: Gustavo Luchini, Emanuel Sergio Girardi, Gabriel Tormena"); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK), "copiar");
        am.put("copiar", new AbstractAction() { public void actionPerformed(ActionEvent e) { editor.copy(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK), "colar");
        am.put("colar", new AbstractAction() { public void actionPerformed(ActionEvent e) { editor.paste(); }});

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK), "recortar");
        am.put("recortar", new AbstractAction() { public void actionPerformed(ActionEvent e) { editor.cut(); }});
    }

    private void acaoNovo() {
        editor.setText("");
        mensagens.setText("");
        arquivoAtual = null;
        atualizarStatus(null);
    }

    private void acaoAbrir() {
        JFileChooser chooser = novoFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                String content = lerArquivo(file);
                editor.setText(content);
                editor.setCaretPosition(0);
                mensagens.setText("");
                arquivoAtual = file;
                atualizarStatus(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao abrir arquivo:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // manter estado
        }
    }

    private void acaoSalvar(boolean saveAs) {
        if (arquivoAtual == null || saveAs) {
            JFileChooser chooser = novoFileChooser();
            chooser.setDialogTitle("Salvar como");
            int result = chooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = ensureTxtExtension(chooser.getSelectedFile());
                try {
                    escreverArquivo(file, editor.getText());
                    arquivoAtual = file;
                    mensagens.setText("");
                    atualizarStatus(file);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                // manter estado
            }
        } else {
            try {
                escreverArquivo(arquivoAtual, editor.getText());
                mensagens.setText("");
                atualizarStatus(arquivoAtual);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar arquivo:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JFileChooser novoFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (.txt)", "txt"));
        chooser.setAcceptAllFileFilterUsed(false);
        return chooser;
    }

    private static File ensureTxtExtension(File f) {
        String name = f.getName();
        if (!name.toLowerCase().endsWith(".txt")) {
            return new File(f.getParentFile(), name + ".txt");
        }
        return f;
    }

    private static String lerArquivo(File file) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            StringBuilder sb = new StringBuilder();
            String line;
            String lineSep = System.lineSeparator();
            while ((line = br.readLine()) != null) {
                sb.append(line).append(lineSep);
            }
            return sb.toString();
        }
    }

    private static void escreverArquivo(File file, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
            bw.write(content);
        }
    }

    private void atualizarStatus(File file) {
        if (file == null) {
            statusLabel.setText(" ");
            return;
        }
        String pasta = file.getParent();
        String nome = file.getName();
        statusLabel.setText("Pasta: " + pasta + " | Arquivo: " + nome);
    }

    private void mostrarMensagemUnica(String msg) {
        mensagens.setText(msg);
    }

    private void acaoCompilar() {
        if (arquivoAtual == null) {
            mensagens.setText("salve o arquivo antes de compilar");
            return;
        }
        String source = editor.getText();
        Lexico lexico = new Lexico();
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();
        lexico.setInput(new StringReader(source));

        try {
            sintatico.parse(lexico, semantico);
            try {
                gerarArquivoIL(semantico.getCodigoObjeto());
                mensagens.setText("programa compilado com sucesso");
            } catch (IOException ex) {
                mensagens.setText("erro ao gerar o arquivo .il: " + ex.getMessage());
            }
        } catch (LexicalError e) {
            int linha = calcularLinha(source, e.getPosition());
            mensagens.setText("linha " + linha + ": erro léxico");
        } catch (SyntaticError e) {
            int linha = calcularLinha(source, e.getPosition());
            mensagens.setText("linha " + linha + ": erro sintático");
        } catch (SemanticError e) {
            int linha = calcularLinha(source, e.getPosition());
            mensagens.setText("linha " + linha + ": " + e.getMessage());
        }
    }

    //gera o arquivo .il na mesma pasta e com o mesmo nome do programa fonte compilado
    private void gerarArquivoIL(String codigoObjeto) throws IOException {
        String nome = arquivoAtual.getName();
        int ponto = nome.lastIndexOf('.');
        if (ponto > 0) {
            nome = nome.substring(0, ponto);
        }
        File arquivoIL = new File(arquivoAtual.getParentFile(), nome + ".il");
        escreverArquivo(arquivoIL, codigoObjeto);
    }

    private int calcularLinha(String text, int position) {
        int linha = 1;
        int limit = Math.min(position, text.length());
        for (int i = 0; i < limit; i++) {
            if (text.charAt(i) == '\n') linha++;
        }
        return linha;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new CompiladorUI().setVisible(true);
        });
    }



    
}
