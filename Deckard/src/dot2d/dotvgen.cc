/*
 * 
 * Copyright (c) 2007-2018, University of California / Singapore Management University
 *   Lingxiao Jiang         <lxjiang@ucdavis.edu> <lxjiang@smu.edu.sg>
 *   Ghassan Misherghi      <ghassanm@ucdavis.edu>
 *   Zhendong Su            <su@ucdavis.edu>
 *   Stephane Glondu        <steph@glondu.net>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the University of California nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 */

/** Generate vectors for PDGs+ASTs parsed from dot files */

#include <getopt.h>
#include <ptree.h>
#include <graph.h>
#include <graphslicer.h>
#include <graphptreemap.h>
#include <tra-gen.h>
#include <vgen-utils.h>
#include "grammars/output/DotTreeCGraph.h"
#include "dotvgen.h"
#include "node-counter.h"

using namespace std;

/** declare some global variables/functions */
map<string, int> name2id; // default to call map's constructor without parameters.
map<int, string> id2name = map<int, string>(); // either way is ok
/* BUG: due to "initialization order fiasco" (http://www.parashift.com/c++-faq-lite/static-init-order.html): NameMap::invalidName may not be initialized yet.
string identifierTypeName = NameMap::invalidName;
 <- fixed it through an additional function that initializes invalidName (as a constructor for invalidName).
 cf. http://www.parashift.com/c++-faq-lite/static-init-order-on-intrinsics.html */
string identifierTypeName = NameMap::getInvalidName();
/* For debugging use only */
ParseTree* global_tree_for_debugging;
static int DEBUG_LEVEL = 1;

void usage(const char * appname)
{
   /* getopt_long should have already printed an error message. */
   cerr << "Usage: " << appname << " [options] [filename] " << endl;
   cerr << "--type-file <name of the file specifying node type names and ids>, or -t " << endl;
   cerr << "--relevant-node-file <name of the file specifying relevant node type names>" << endl;
   cerr << "--leaf-node-file <name of the file specifying atomic/leaf node type names>" << endl;
   cerr << "--parent-node-file <name of the file specifying valid parent node type names>" << endl;
   cerr << "--context-node-file <name of the file specifying contextual node type names>" << endl;
   cerr << "--pdg-dot <the PDG .dot file name>, or -p " << endl;
   cerr << "--ast-dot <the AST .dot file name>, or -a " << endl;
   cerr << "--output-file <vector file name>, or -o (default: ast-dot-name plus '.<num>.vec')" << endl;
   cerr << "--config-file <parameter file name>, or -c (default: not use)." << endl
        << "  Currently, the file can only contain three integers." << endl;
   cerr << "--mim-token-number <number>, or -m (default: 30)" << endl;
   cerr << "--stride <number>, or -s (default: 1)" << endl;
   cerr << "--merge-list-size <number> (default: not used)" << endl;
   cerr << "--start-line-number <number> (default: all lines)" << endl;
   cerr << "--end-line-number <number> (default: same as start-line-number)" << endl;
   cerr << "--gamma <number>, or -y (default: 3)" << endl;
   cerr << "--help or -h" << endl;
   cerr << "Later options can override previous options. The actual overriding order is undefined; pls avoid specifying the same parameter twice." << endl;
}

int main( int argc, char **argv )
{
   // use getopt_long; should also work under cygwin
   static const struct option longOpts[] = {
         { "type-file", required_argument, NULL, 't' },
         { "identifier-type-name", required_argument, NULL, 0 },
         { "relevant-node-file", required_argument, NULL, 0 },
         { "leaf-node-file", required_argument, NULL, 0 },
         { "parent-node-file", required_argument, NULL, 0 },
         { "context-node-file", required_argument, NULL, 0 },
         { "pdg-dot", required_argument, NULL, 'p' },
         { "ast-dot", required_argument, NULL, 'a' },
         { "output-file", required_argument, NULL, 'o' },
         { "config-file", required_argument, NULL, 'c' },
         { "mim-token-number", required_argument, NULL, 'm' },
         { "stride", required_argument, NULL, 's' },
         { "merge-list-size", required_argument, NULL, 0 },
         { "start-line-number", required_argument, NULL, 0 },
         { "end-line-number", required_argument, NULL, 0 },
         { "gamma", required_argument, NULL, 'y' },
         { "help", no_argument, NULL, 'h'},
         { NULL, no_argument, NULL, 0 }
   };

   const char * typefilename = NULL;
   const char * relevantnodefilename = NULL;
   const char * leafnodefilename = NULL;
   const char * parentnodefilename = NULL;
   const char * contextnodefilename = NULL;
   const char * pdgdotfilename = NULL;
   const char * astdotfilename = NULL;
   const char * outputfilename = NULL;
   const char * configfilename = NULL;
   int mergeTokens = 30, mergeStride = 1, mergeLists = 3;
   int startline = 0, endline = -1; /* default: all lines */
   float gamma = 3.0;

   // processing command line options
   int c = 0, longIndex = 0;
   while ((c = getopt_long (argc, argv, "t:p:a:o:c:m:s:y:h", longOpts, &longIndex)) != -1) {
     switch (c) {
     case 't':
        typefilename = optarg;
        break;
     case 'p':
        pdgdotfilename = optarg;
        break;
     case 'a':
        astdotfilename = optarg;
        break;
     case 'o':
        outputfilename = optarg;
        break;
     case 'c':
        configfilename = optarg;
        TraGenMain::getParameters(configfilename, mergeTokens, mergeStride, mergeLists);
        break;
     case 'm':
        if ( sscanf(optarg, "%d", &mergeTokens)<=0 ) {
           cerr << "Warning: invalid mergeTokens in option -m (default 30): " << optarg << endl;
           mergeTokens = 30;
        }
        break;
     case 's':
        if ( sscanf(optarg, "%d", &mergeStride)<=0 ) {
           cerr << "Warning: invalid mergeStride in option -s (default 1): " << optarg << endl;
           mergeStride = 1;
        }
        break;
     case 'y':
        if ( sscanf(optarg, "%f", &gamma)<=0 ) {
           cerr << "Warning: invalid gamma in option -y (default 3.0): " << optarg << endl;
           gamma = 3.0;
        }
        break;
     case 0: /* for long options w/o a short name */
        if( compare_string_nocase( "relevant-node-file", longOpts[longIndex].name ) == 0 ) {
           relevantnodefilename = optarg;
        } else if( compare_string_nocase( "leaf-node-file", longOpts[longIndex].name ) == 0 ) {
           leafnodefilename = optarg;
        } else if( compare_string_nocase( "parent-node-file", longOpts[longIndex].name ) == 0 ) {
           parentnodefilename = optarg;
        } else if( compare_string_nocase( "context-node-file", longOpts[longIndex].name ) == 0 ) {
           contextnodefilename = optarg;
        } else if ( compare_string_nocase("identifier-type-name", longOpts[longIndex].name)==0 ) {
           identifierTypeName = optarg;
        } else if( compare_string_nocase( "merge-list-size", longOpts[longIndex].name ) == 0 ) {
           if ( sscanf(optarg, "%d", &mergeLists)<=0 ) {
              cerr << "Warning: invalid mergeLists in option --merge-list-size (default 3): " << optarg << endl;
              mergeLists = 3;
           }
        } else if ( compare_string_nocase( "start-line-number", longOpts[longIndex].name ) == 0 ) {
           if ( sscanf(optarg, "%d", &startline)<=0 ) {
              cerr << "Warning: invalid startline in option --start-line-number (default: all lines): " << optarg << endl;
              startline = 0;
           }
        } else if ( compare_string_nocase( "end-line-number", longOpts[longIndex].name ) == 0 ) {
           if ( sscanf(optarg, "%d", &endline)<=0 ) {
              cerr << "Warning: invalid endline in option --end-line-number (default: same as --start-line-number): " << optarg << endl;
              endline = -1;
           }
        }
        break;
     case 'h':
     case '?':
        usage(argv[0]);
        return 0;
     default:
        /* unreachable */;
        usage(argv[0]);
        throw "unhandled command line option: " + c;
     }
   }

   // additional processing of options:
   if(endline<0)
     endline = startline;

   // ignore other command line parameters
   if (optind < argc) {
      cerr << "Warning: unparsed options are ignored." << endl;
   }
   cerr << "Merging parameters: " << mergeTokens << ", " << mergeStride
        << ", " << mergeLists <<", " << gamma << endl;

   // basic validity checking of the command line options:
   if ( typefilename==NULL ) {
      cerr << "Error: --type-file <filename> cannot be empty" << endl;
      return 1;
   }
   if ( pdgdotfilename==NULL ) {
      cerr << "Error: --pdg-dot <filename> cannot be empty" << endl;
      return 1;
   }
   if ( astdotfilename==NULL ) {
      cerr << "Error: --ast-dot <filename> cannot be empty" << endl;
      return 1;
   }
   if ( relevantnodefilename==NULL ) {
      cerr << "Error: --relevant-node-file <filename> cannot be empty" << endl;
      return 1;
   }
   if ( leafnodefilename==NULL ) {
      cerr << "Error: --leaf-node-file <filename> cannot be empty" << endl;
      return 1;
   }
   if ( parentnodefilename==NULL ) {
      cerr << "Error: --parent-node-file <filename> cannot be empty" << endl;
      return 1;
   }
   
   // read the type-file and initialize some global variables
   NameMap nmap = NameMap::readNamesIDs(typefilename);
   GraphTreeMapper::getFakeTypeID() = nmap.getOrAddNameId(GraphTreeMapper::getFakeTypeName());
   name2id = nmap.getNameIDMap();
   id2name = nmap.getIDNameMap();
   if ( DEBUG_LEVEL>2 ) {
      nmap.printNamesIDs();
      nmap.printIDsNames();
   }
   set<string> relevantNodes = NameMap::readNames(relevantnodefilename);
   set<string> leafNodes = NameMap::readNames(leafnodefilename);
   set<string> parentNodes = NameMap::readNames(parentnodefilename);
   set<string> contextualNodes = NameMap::readNames(contextnodefilename);
   setContextualNodes(contextualNodes);

   // parse the pdg-dot file
   Graph* dotPDG = pDotTreeCParserGraph(pdgdotfilename);
   if ( DEBUG_LEVEL>2 ) {
      dotPDG->outputGraph2Dot((string(pdgdotfilename)+".pdg").c_str(), true);
   }
   
   // parse the ast-dot file
   Graph* dotASTGraph = pDotTreeCParserGraph(astdotfilename);
   if ( DEBUG_LEVEL>2 ) {
      dotASTGraph->outputGraph2Dot((string(astdotfilename)+".ast").c_str(), true);
   }
   
   assert(dotPDG!=NULL && dotASTGraph!=NULL);

   // setup vec file for output
   string outfilestring = outputfilename==NULL ? (dotPDG->graph_functionSig + ".vec") : outputfilename;
   FILE * outfile = NULL;
   outfile = fopen(outfilestring.c_str(), "w");
   if( outfile==NULL ) {
      cerr << "Warning: Can't open file for writing vectors; stop: " << outfilestring << endl;
      return 65;
   }

   // construct semantic threads
   vector<Graph*> st = GraphSlicer::semanticThreads(dotPDG, SlicingCriteriaAcceptAll::instance(), gamma);
   GraphTreeMapper gmapper("line");
   int stcount = 0;
   if ( DEBUG_LEVEL>0 ) {
      cerr << "INFO: Total # of ST: " << st.size() << endl;
   }
   for(vector<Graph*>::const_iterator it = st.begin();
         it!=st.end(); ++it) {
      stcount++;

      Graph* st = *it;
      if ( DEBUG_LEVEL>1 ) {
         cerr << "DEBUG: writing to dot file ST#" << stcount << endl;
         stringstream ofname;
         ofname << pdgdotfilename << ".pdg.st." << stcount;
         st->outputGraph2Dot(ofname.str().c_str(), true);
      }
      Tree* stast = gmapper.graph2tree(st, dotASTGraph); // a new tree is constructed for the 'st'
      
      // call vgen with the tree stast
      ParseTree p(stast, nmap.currentLastID()+1, &id2name, &name2id);
      ParseTree::setNodeIDs(p.relevantNodes, relevantNodes);
      ParseTree::setNodeIDs(p.leafNodes, leafNodes);
      ParseTree::setNodeIDs(p.validParents, parentNodes);
      //p.setNodeIDs(p.mergeableNodes, mergeableNodes);

      p.filename = dotPDG->graph_functionSig;

      // for debugging use only in vector generator.
      global_tree_for_debugging = &p;
      if ( DEBUG_LEVEL>1 ) {
         cerr << "INFO: typeCount before init() = " << p.typeCount() << endl;
      }

      /* TODO: why vectors are empty? possible reason:
       * - no token (no Terminal) (fixed);
       * - no linenumber (to set line ranges for nodes) (partially fixed; to deal with line ranges)
       */
      ASTTraGenMain t(&p, mergeTokens, mergeStride, mergeLists, typefilename, outfile);
      t.run(startline, endline); // also, update token counts for the nodes.
      if ( DEBUG_LEVEL>1 ) {
         cerr << "DEBUG: writing the tree to dot file for ST#" << stcount << endl;
         stringstream ofname;
         ofname << pdgdotfilename << ".pdg.st." << stcount << ".ast";
         p.outputParseTree2Dot(ofname.str().c_str(), true);
      }
      global_tree_for_debugging = NULL;

      // delete the ST to save memory; may leave it to OS though
      delete st;
   }

   fclose(outfile);
   cerr << "Check vectors (if any) in output file: " << outfilestring << endl;

   // clear up memory; not that necessary: could leave it to OS
   dotPDG->deleteGraphNodes();
   delete dotPDG;
   dotASTGraph->deleteGraphNodes();
   delete dotASTGraph;

   return 0;
}


/************************************
 * class ASTTraGenMain
 * 
 */
ASTTraGenMain::
ASTTraGenMain(ParseTree* rt, const char * fn, const char * tn, FILE * out)
  : TraGenMain(rt, fn, out)
{
   if ( token_counter!=NULL )
      delete token_counter;

   token_counter = new NodeTokenCounter(*vecGen_config); // NOT depend on vecGen_config->mergeTokens/moveStride.
   static_cast<NodeTokenCounter*>(token_counter)->init(tn);
}

ASTTraGenMain::
ASTTraGenMain(ParseTree* rt, int mergeTokens, int mergeStride, int mergeLists, const char * tn, FILE * out)
  : TraGenMain(rt, mergeTokens, mergeStride, mergeLists, out)
{
   if ( token_counter!=NULL )
      delete token_counter;

   token_counter = new NodeTokenCounter(*vecGen_config); // NOT depend on vecGen_config->mergeTokens/moveStride.
   static_cast<NodeTokenCounter*>(token_counter)->init(tn);
}

