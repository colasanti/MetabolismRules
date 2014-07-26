/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package metabolismrules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 *
 * @author ric
 */
public class MetabolismRules {

    public MetabolismRules() {

    }

    private void loadRawData(String inFileName) {
        try {
            File inFile = new File(inFileName);
            FileReader fileReader = new FileReader(inFile);
            BufferedReader reader = new BufferedReader(fileReader);

            String line;
            while ((line = reader.readLine()) != null) {
                String delims = "\t";
                String[] tokens = line.split(delims);
                String classType = "";
                if ((tokens[4].equals("faculatative"))
                        || (tokens[4].equals("facultative"))) {
                    classType = "faculatative";
                }
                if ((tokens[4].equals("anaerobic"))
                        || (tokens[4].equals("anaerobe"))) {
                    classType = "anaerobe";
                }
                if ((tokens[4].equals("aerobic"))
                        || (tokens[4].equals("aerobe"))) {
                    classType = "aerobe";
                }
                if (!"".equals(classType)) {
                    int tcaCount = 0;
                    int mkCount = 0;
                    int qCount = 0;
                    int fCount = 0;
                    String[] tca = {"Succinate dehydrogenase iron-sulfur protein (EC 1.3.99.1)" ,
                                    "Citrate synthase (si) (EC 2.3.3.1)",
                                    "2-oxoglutarate dehydrogenase E1 component (EC 1.2.4.2)",
                                    "Fumarate hydratase class I, aerobic (EC 4.2.1.2)",
                                    "Dihydrolipoamide succinyltransferase component (E2) of 2-oxoglutarate dehydrogenase complex (EC 2.3.1.61)",
                                    "Succinyl-CoA ligase [ADP-forming] alpha chain (EC 6.2.1.5)",
                                    "Archaeal succinyl-CoA ligase [ADP-forming] alpha chain (EC 6.2.1.5)"
                    };
                    String[] mk1 = {"Menaquinone-specific isochorismate synthase (EC 5.4.4.2)",
                                    "Naphthoate synthase (EC 4.1.3.36)",
                                    "O-succinylbenzoate synthase (EC 4.2.1.113)",
                                    "2-succinyl-6-hydroxy-2,4-cyclohexadiene-1-carboxylate synthase (EC 4.2.99.20)"
                        
                    };
                    String[] mk2 = {"Menaquinone via futalosine step 1",
                                    "Menaquinone via futalosine step 2",
                                    "Menaquinone via futalosine step 3",
                                    "Menaquinone via futalosine step 4"
                    };
                    String[] q = {"2-octaprenyl-6-methoxyphenol hydroxylase (EC 1.14.13.-)",
                                  "2-octaprenyl-3-methyl-6-methoxy-1,4-benzoquinol hydroxylase (EC 1.14.13.-)",
                                  "Chorismate--pyruvate lyase (EC 4.1.3.40)"
                    };
                    String[] f = {"Aerobic respiration control protein arcA",
                                  "Aerobic respiration control sensor protein arcB (EC 2.7.3.-)"
                    };
                    
                    for (int i = 5; i < tokens.length; i++) {
                        String attribute = tokens[i];
                        tcaCount += testMembership(attribute,tca);
                        mkCount += testMembership(attribute,mk1);
                        mkCount += testMembership(attribute,mk2);
                        qCount += testMembership(attribute,q);
                        fCount += testMembership(attribute,f);
                    }
                    String predict = prediction(tcaCount,mkCount,qCount,fCount);
                    System.out.println(classType+" "+predict+" "+classType.equals(predict));
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bugger");
        }

    }
    private int testMembership(String attribute, String[] rules){
        for(String rule : rules){
            if (attribute.equals(rule)){
                return 1;
            }
        }
        return 0;
    }

    private String prediction(int tcaCount,int mkCount,int qCount,int fCount){
        if(qCount>=3 && mkCount>=3){
            return "faculatative";
        }
        if(fCount >=2){
            return "faculatative";
        }
        if((qCount>=2 || mkCount >=3)|| tcaCount>=5){
            return "aerobe";
        }
        if ((qCount<2 || mkCount <2)|| tcaCount>=5){
             return "aerobe";
        }
        if ((qCount<2 || mkCount <2)|| tcaCount < 5){
             return "anaerobe";
        }
        return "none";
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MetabolismRules m = new MetabolismRules();
        m.loadRawData("/home/ric/Programming/Classifiers/ClassifierPaper/Data/classiferPaperFunctions010614.txt");
    }

}
