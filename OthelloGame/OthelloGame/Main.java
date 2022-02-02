import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
class Main
{
    final JFrame f;
    static JLabel jStat,jNote;
    static JButton jPass,jShow;
    static JButton JB[][] = new JButton[8][8];
    final static JLabel k=new JLabel("1");
    static int mat[][]=new int[8][8];
    static int game=0;
    static boolean shown=false;
    Main()
    {
        f=new JFrame("Othello PvP");
        f.getContentPane().setBackground(new Color(80,48,12));
        f.pack();
        f.setBackground(new Color(80,48,12));
        f.setSize(646,830);
        f.setResizable(false);
        f.setVisible(true);
        int i,j;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                JB[i][j]=new JButton("");
                JB[i][j].setMargin(new Insets(0, 0, 0, 0));
                JB[i][j].setOpaque(true);
                JB[i][j].setBackground(new Color(78,146,82));
                JB[i][j].setForeground(Color.white);
                JB[i][j].setBounds(40+70*j,40+70*i,70,70);
                JB[i][j].setBorder(new LineBorder(new Color(0,90,0)));
                JB[i][j].setFont(new Font("Courier", Font.PLAIN, 80));
                JB[i][j].setPreferredSize(new Dimension(70,70));
            }
        }
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                f.add(JB[i][j]);
            }
        }
        jStat=new JLabel("");
        //jStat.setMargin(new Insets(0, 0, 0, 0));
        jStat.setOpaque(true);
        jStat.setBounds(2,640,636,46);
        jStat.setBackground(new Color(51,0,7));
        jStat.setForeground(new Color(240,230,181));
        jStat.setText("White - 2    Black - 2");
        jStat.setFont(new Font("Arial", Font.PLAIN, 40));
        jStat.setBorder(BorderFactory.createEmptyBorder());
        jStat.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(jStat);
        jNote=new JLabel("");
        //jNote.setMargin(new Insets(0, 0, 0, 0));
        jNote.setOpaque(true);
        jNote.setBounds(2,690,636,46);
        jNote.setBackground(new Color(51,0,7));
        jNote.setForeground(new Color(240,230,181));
        jNote.setText("Black's turn");
        jNote.setFont(new Font("Arial", Font.PLAIN, 24));
        jNote.setBorder(BorderFactory.createEmptyBorder());
        jNote.setHorizontalAlignment(SwingConstants.CENTER);
        f.add(jNote);
        jPass=new JButton("");
        jPass.setMargin(new Insets(0, 0, 0, 0));
        jPass.setOpaque(true);
        jPass.setBounds(4,740,314,46);
        jPass.setBackground(new Color(51,0,7));
        jPass.setForeground(new Color(240,230,181));
        jPass.setText("PASS MOVE");
        jPass.setFont(new Font("Arial", Font.BOLD, 24));
        jPass.setBorder(new LineBorder(new Color(240,230,181)));
        //jPass.setBorder(BorderFactory.createEmptyBorder());
        f.add(jPass);
        jShow=new JButton("");
        jShow.setMargin(new Insets(0, 0, 0, 0));
        jShow.setOpaque(true);
        jShow.setBounds(322,740,314,46);
        jShow.setBackground(new Color(51,0,7));
        jShow.setForeground(new Color(240,230,181));
        jShow.setText("SHOW MOVE");
        jShow.setFont(new Font("Arial", Font.BOLD, 24));
        jShow.setBorder(new LineBorder(new Color(240,230,181)));
        //jShow.setBorder(BorderFactory.createEmptyBorder());
        f.add(jShow);
        jPass.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(shown)
                    hide();
                pass();                      
            }          
        });
        jShow.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent event) 
            {
                if(shown)
                    hide();
                else
                    show();                      
            }          
        });
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                JB[i][j].addActionListener(new ActionListener() 
                {
                    @Override
                    public void actionPerformed(ActionEvent event) 
                    {
                        for (int i=0;i<8;i++)
                        {
                            for (int j=0;j<8;j++)
                            {
                                if (event.getSource() == JB[i][j])
                                {
                                    if(game==0)
                                    {
                                        if(shown)
                                            hide();
                                        click(i,j,Integer.parseInt(k.getText()));
                                        k.setText(String.valueOf(Integer.parseInt(k.getText())+1));
                                    }
                                } 
                            }
                        }                 
                    }          
                });
            }
        }
        JLabel endLabel=new JLabel();
        f.add(endLabel);
        initialise();
    }
    private static void click(int x,int y,int z)
    {
        int f=mat[x][y];
        if(z%2!=0)
        {
            JB[x][y].setForeground(Color.black);
            mat[x][y]=1;
        }
        else
        {
            JB[x][y].setForeground(Color.white);
            mat[x][y]=2;
        }
        JB[x][y].setText("⚫");
        int e=0;
        if(f==0)
            e=flip(x,y,z);
        if(e==0)
        {    
            k.setText(String.valueOf(Integer.parseInt(k.getText())-1));
            mat[x][y]=f;
            if(f==0)
                JB[x][y].setText("");
            if(f==1)
                JB[x][y].setForeground(Color.black);
            if(f==2)
                JB[x][y].setForeground(Color.white);
            jNote.setText((jNote.getText()).substring(0,12)+"  [Invalid move, try again]");
        }
        else
        {
            if(z%2!=0)
                jNote.setText("White's turn");
            else
                jNote.setText("Black's turn");
        }
        count();
    }
    private static int flip(int x,int y,int z)
    {
        int i,j,m,n,d=0;
        for(i=0;i<8;i++)
        {
            for(j=0;j<8;j++)
            {
                if(z%2!=0 && mat[i][j]==1)
                {
                    if(j==y && i<x-1)
                    {
                        int c=0;
                        for(int k=i+1;k<x;k++)
                            if(mat[k][y]!=2)
                                c++;
                        if(c==0)
                        {
                            for(int k=i+1;k<x;k++)
                            {
                                mat[k][y]=1;
                                JB[k][y].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(j==y && i>x+1)
                    {
                        int c=0;
                        for(int k=x+1;k<i;k++)
                            if(mat[k][y]!=2)
                                c++;
                        if(c==0)
                        {
                            for(int k=x+1;k<i;k++)
                            {
                                mat[k][y]=1;
                                JB[k][y].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(i==x && j<y-1)
                    {
                        int c=0;
                        for(int k=j+1;k<y;k++)
                            if(mat[x][k]!=2)
                                c++;
                        if(c==0)
                        {
                            for(int k=j+1;k<y;k++)
                            {
                                mat[x][k]=1;
                                JB[x][k].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(i==x && j>y+1)
                    {
                        int c=0;
                        for(int k=y+1;k<j;k++)
                            if(mat[x][k]!=2)
                                c++;
                        if(c==0)
                        {
                            for(int k=y+1;k<j;k++)
                            {
                                mat[x][k]=1;
                                JB[x][k].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(x-i==y-j && x-i>1 && y-j>1)
                    {
                        int c=0;
                        for(m=i+1,n=j+1;m<x && n<y;m++,n++)
                            if(mat[m][n]!=2)
                                c++;
                        if(c==0)
                        {
                            for(m=i+1,n=j+1;m<x && n<y;m++,n++)
                            {
                                mat[m][n]=1;
                                JB[m][n].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(x-i==j-y && x-i>1 && j-y>1)
                    {
                        int c=0;
                        for(m=i+1,n=j-1;m<x && n>y;m++,n--)
                            if(mat[m][n]!=2)
                                c++;
                        if(c==0)
                        {
                            for(m=i+1,n=j-1;m<x && n>y;m++,n--)
                            {
                                mat[m][n]=1;
                                JB[m][n].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(i-x==y-j && i-x>1 && y-j>1)
                    {
                        int c=0;
                        for(m=i-1,n=j+1;m>x && n<y;m--,n++)
                            if(mat[m][n]!=2)
                                c++;
                        if(c==0)
                        {
                            for(m=i-1,n=j+1;m>x && n<y;m--,n++)
                            {
                                mat[m][n]=1;
                                JB[m][n].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                    if(i-x==j-y && i-x>1 && j-y>1)
                    {
                        int c=0;
                        for(m=i-1,n=j-1;m>x && n>y;m--,n--)
                            if(mat[m][n]!=2)
                                c++;
                        if(c==0)
                        {
                            for(m=i-1,n=j-1;m>x && n>y;m--,n--)
                            {
                                mat[m][n]=1;
                                JB[m][n].setForeground(Color.black);
                            }
                            d++;
                        }
                    }
                }
                if(z%2==0 && mat[i][j]==2)
                {
                    if(j==y && i<x-1)
                    {
                        int c=0;
                        for(int k=i+1;k<x;k++)
                            if(mat[k][y]!=1)
                                c++;
                        if(c==0)
                        {
                            for(int k=i+1;k<x;k++)
                            {
                                mat[k][y]=2;
                                JB[k][y].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(j==y && i>x+1)
                    {
                        int c=0;
                        for(int k=x+1;k<i;k++)
                            if(mat[k][y]!=1)
                                c++;
                        if(c==0)
                        {
                            for(int k=x+1;k<i;k++)
                            {
                                mat[k][y]=2;
                                JB[k][y].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(i==x && j<y-1)
                    {
                        int c=0;
                        for(int k=j+1;k<y;k++)
                            if(mat[x][k]!=1)
                                c++;
                        if(c==0)
                        {
                            for(int k=j+1;k<y;k++)
                            {
                                mat[x][k]=2;
                                JB[x][k].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(i==x && j>y+1)
                    {
                        int c=0;
                        for(int k=y+1;k<j;k++)
                            if(mat[x][k]!=1)
                                c++;
                        if(c==0)
                        {
                            for(int k=y+1;k<j;k++)
                            {
                                mat[x][k]=2;
                                JB[x][k].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(x-i==y-j && x-i>1 && y-j>1)
                    {
                        int c=0;
                        for(m=i+1,n=j+1;m<x && n<y;m++,n++)
                            if(mat[m][n]!=1)
                                c++;
                        if(c==0)
                        {
                            for(m=i+1,n=j+1;m<x && n<y;m++,n++)
                            {
                                mat[m][n]=2;
                                JB[m][n].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(x-i==j-y && x-i>1 && j-y>1)
                    {
                        int c=0;
                        for(m=i+1,n=j-1;m<x && n>y;m++,n--)
                            if(mat[m][n]!=1)
                                c++;
                        if(c==0)
                        {
                            for(m=i+1,n=j-1;m<x && n>y;m++,n--)
                            {
                                mat[m][n]=2;
                                JB[m][n].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(i-x==y-j && i-x>1 && y-j>1)
                    {
                        int c=0;
                        for(m=i-1,n=j+1;m>x && n<y;m--,n++)
                            if(mat[m][n]!=1)
                                c++;
                        if(c==0)
                        {
                            for(m=i-1,n=j+1;m>x && n<y;m--,n++)
                            {
                                mat[m][n]=2;
                                JB[m][n].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                    if(i-x==j-y && i-x>1 && j-y>1)
                    {
                        int c=0;
                        for(m=i-1,n=j-1;m>x && n>y;m--,n--)
                            if(mat[m][n]!=1)
                                c++;
                        if(c==0)
                        {
                            for(m=i-1,n=j-1;m>x && n>y;m--,n--)
                            {
                                mat[m][n]=2;
                                JB[m][n].setForeground(Color.white);
                            }
                            d++;
                        }
                    }
                }
            }
        }
        return d;
    }
    private static void initialise()
    {
        JB[3][3].setForeground(Color.white);
        JB[3][3].setText("⚫");
        JB[4][4].setForeground(Color.white);
        JB[4][4].setText("⚫");
        JB[3][4].setForeground(Color.black);
        JB[3][4].setText("⚫");
        JB[4][3].setForeground(Color.black);
        JB[4][3].setText("⚫");
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                mat[i][j]=0;  
        mat[3][3]=2;
        mat[4][4]=2;
        mat[3][4]=1;
        mat[4][3]=1;
    }
    private static void count()
    {
        int b=0,w=0;
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
            {
                if(mat[i][j]==1)
                    b++;
                if(mat[i][j]==2)
                    w++;
            }
        jStat.setText("White - "+w+"    Black - "+b);
        if(b+w==64)
        {
            if(b>w)
                jNote.setText("Black WINS (All squares filled)");
            else if(w>b)
                    jNote.setText("White WINS (All squares filled)");
                 else
                    jNote.setText("Game TIED (All squares filled)");
            game=1;        
        }
        if(noValid(1,0) && noValid(2,0))
        {
            if(b>w)
                jNote.setText("Black WINS (No valid moves left)");
            else if(w>b)
                    jNote.setText("White WINS (No valid moves left)");
                 else
                    jNote.setText("Game TIED (No valid moves left)");
            game=1;        
        }
    }
    private static boolean noValid(int p,int r)
    {
        int i,j,x,y,m,n,c;
        int q=3-p;
        boolean ret=true;
        for(x=0;x<8;x++)
        {
            for(y=0;y<8;y++)
            {
                for(i=0;i<8;i++)
                {
                    for(j=0;j<8;j++)
                    {
                        if(mat[i][j]==p)
                        {
                            if(j==y && i<x-1)
                            {
                                c=0;
                                for(int k=i+1;k<x;k++)
                                    if(mat[k][y]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(j==y && i>x+1)
                            {
                                c=0;
                                for(int k=x+1;k<i;k++)
                                    if(mat[k][y]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(i==x && j<y-1)
                            {
                                c=0;
                                for(int k=j+1;k<y;k++)
                                    if(mat[x][k]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(i==x && j>y+1)
                            {
                                c=0;
                                for(int k=y+1;k<j;k++)
                                    if(mat[x][k]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(x-i==y-j && x-i>1 && y-j>1)
                            {
                                c=0;
                                for(m=i+1,n=j+1;m<x && n<y;m++,n++)
                                    if(mat[m][n]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(x-i==j-y && x-i>1 && j-y>1)
                            {
                                c=0;
                                for(m=i+1,n=j-1;m<x && n>y;m++,n--)
                                    if(mat[m][n]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(i-x==y-j && i-x>1 && y-j>1)
                            {
                                c=0;
                                for(m=i-1,n=j+1;m>x && n<y;m--,n++)
                                    if(mat[m][n]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                            if(i-x==j-y && i-x>1 && j-y>1)
                            {
                                c=0;
                                for(m=i-1,n=j-1;m>x && n>y;m--,n--)
                                    if(mat[m][n]!=q)
                                        c++;
                                if(c==0)
                                {
                                    if(r==1 && mat[x][y]==0)
                                        JB[x][y].setBackground(new Color(219,221,117));
                                    ret=false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }
    private static void pass()
    {
        int z=Integer.parseInt(k.getText());
        if((z%2!=0 && noValid(1,0))||(z%2==0 && noValid(2,0)))
            k.setText(String.valueOf(z+1));
        else
            jNote.setText((jNote.getText()).substring(0,12)+"  [Valid move possible]");
    }
    private static void show()
    {
        int z=Integer.parseInt(k.getText());
        boolean val=noValid(2-(z%2),1);
        shown=true;
        jShow.setText("HIDE MOVE");
    }
    private static void hide()
    {
        for(int i=0;i<8;i++)
            for(int j=0;j<8;j++)
                JB[i][j].setBackground(new Color(78,146,82));
        shown=false;
        jShow.setText("SHOW MOVE");
    }
    public static void main(String args[])
    {
        Main obj=new Main();
    }
}