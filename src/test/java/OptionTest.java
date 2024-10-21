import base.TestSetUp;

import com.ib.client.Bar;
import com.ib.client.Contract;
import com.ib.client.Decimal;
import com.ib.client.Order;
import com.trading.scan.*;
import com.trading.support.EMACalculator;
//import com.trading.support.SMACalculator;
//import com.trading.tickers.FtpDownloader;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class OptionTest extends TestSetUp {


    //@Test
    public void testcurrency() throws Exception{
        Contract eurusdContract = new Contract();
        eurusdContract.symbol("EUR");
        eurusdContract.secType("CASH");
        eurusdContract.currency("USD");
        eurusdContract.exchange("IDEALPRO");
        m_client.reqMktDepth(1001, eurusdContract, 5, false, null);


        Thread.sleep(3000);
        System.out.println("TESTTT#######@@@@@");


    }

    @Test
    public void testDownload(){

    }
   // @Test
    public void testReadFile() throws IOException, URISyntaxException {

        Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader()
                .getResource("nasdaqlisted.txt")).toURI());

        Stream<String> lines = Files.lines(path);
        Stream<String> limited =  lines.limit(10);
        List<String> tickers = new ArrayList<>();
        limited.forEach(line ->{

            tickers.add(line.split("\\|")[0]);
        });
        tickers.remove(0);
        System.out.println(tickers);

    }
    //@Test
    public void testFast(){
//FtpDownloader ftpDownloader = new FtpDownloader();
    //    ftpDownloader.downloadToText();
        //System.out.println("fast===" + );
    }
    //@Test
    public void testSmaMrkt() throws Exception{
        Contract contract = new Contract();
        m_client.reqHistoricalData(1000 + 10, new USStockContract("AMZN"), "", "200 D", "1 day", "MIDPOINT", 1, 1, false, null);
        Thread.sleep(3000);
       // Calculator calculator = new VolumeCalculator();
        //calculator.calculate(wrapper.getList());
        List<Bar> list = (List<Bar>) wrapper.getList();
        EMACalculator smaCalculator = new EMACalculator();
        double smaLastValue= smaCalculator.calculate(new ArrayList<>(
                wrapper.getList()), 200);
        double lastClosePrice = list.get(list.size() -1).close();
        if(lastClosePrice> smaLastValue ){
               System.out.println("sma value====");
        }
       // System.out.printf("TESTTT#######@@@@@%s%n", );
    }

    //@Test
    public void test(){
     Long seconds =   Instant.now().getEpochSecond();
     System.out.println(seconds);
    }
    //@Test
    public void testEMA() throws Exception{
        Contract contract = new Contract();
        System.out.println("STARETEDD");
        assert  m_client.isConnected();
        m_client.reqHistoricalData(1000 + 10, new USStockContract("SMCI"), "", "40 D", "1 hour", "MIDPOINT", 1, 1, false, null);
        Thread.sleep(3000);
        System.out.println("size===="+wrapper.getList().size());
        EMACalculator smaCalculator = new EMACalculator();
        smaCalculator.calculateEMA(new ArrayList<>(wrapper.getList()), 13);
    }

   // @Test
    public void sp500ticker() throws IOException {
        //SP500Scraper sp500Scraper = new SP500Scraper();
        //System.out.println("fetch==="+ sp500Scraper.fetch());
    }

    @Test
    public void scannerCross() throws InterruptedException {
        //acmr axdx
        //TickerReader tickerReader = new TickerReader();
      //  List<String> tickers = tickerReader.tickers();
       Scan scan = new CrossScan(12,26,5, 200);
      // String [] tickers = {"AAL"};
       String [] tickers = {"AACG","AADI","AADR","AAL","AAME","AAOI","AAON","AAPB","AAPD","AAPL","AAPU","AAXJ","ABAT","ABCL","ABCS","ABEO","ABL","ABLLL","ABLLW","ABLV","ABLVW","ABNB","ABOS","ABSI","ABTS","ABUS","ABVC","ABVE","ABVEW","ABVX","ACAB","ACABU","ACABW","ACAD","ACB","ACCD","ACDC","ACET","ACGL","ACGLN","ACGLO","ACHC","ACHL","ACHV","ACIC","ACIU","ACIW","ACLS","ACLX","ACMR","ACNB","ACNT","ACON","ACONW","ACRS","ACRV","ACST","ACT","ACTG","ACTU","ACVA","ACWI","ACWX","ACXP","ADAG","ADAP","ADBE","ADD","ADEA","ADGM","ADI","ADIL","ADMA","ADN","ADNWW","ADP","ADPT","ADSE","ADSEW","ADSK","ADTN","ADTX","ADUS","ADV","ADVM","ADVWW","ADXN","AEAE","AEAEU","AEAEW","AEHL","AEHR","AEI","AEIS","AEMD","AENT","AENTW","AEP","AERT","AERTW","AEYE","AFBI","AFCG","AFJK","AFJKR","AFJKU","AFMD","AFRI","AFRIW","AFRM","AFYA","AGAE","AGBA","AGBAW","AGEN","AGFY","AGIO","AGIX","AGMH","AGMI","AGNC","AGNCL","AGNCM","AGNCN","AGNCO","AGNCP","AGNG","AGRI","AGYS","AGZD","AHCO","AHG","AIA","AIEV","AIFF","AIHS","AILE","AILEW","AIMAU","AIMAW","AIMBU","AIMD","AIMDW","AIOT","AIP","AIPI","AIQ","AIRE","AIRG","AIRJ","AIRJW","AIRL","AIRR","AIRS","AIRT","AIRTP","AISP","AISPW","AITR","AITRR","AITRU","AIXI","AKAM","AKAN","AKBA","AKRO","AKTS","AKTX","AKYA","ALAB","ALAR","ALBT","ALCE","ALCO","ALCY","ALCYU","ALCYW","ALDX","ALEC","ALF","ALFUU","ALFUW","ALGM","ALGN","ALGS","ALGT","ALHC","ALKS","ALKT","ALLK","ALLO","ALLR","ALLT","ALMS","ALNT","ALNY","ALOT","ALPP","ALRM","ALRN","ALRS","ALSA","ALSAR","ALSAU","ALSAW","ALT","ALTI","ALTO","ALTR","ALTS","ALTY","ALVO","ALVOW","ALVR","ALXO","ALZN","AMAL","AMAT","AMBA","AMCX","AMD","AMDL","AMDS","AMED","AMGN","AMID","AMIX","AMKR","AMLI","AMLX","AMPG","AMPGW","AMPH","AMPL","AMRK","AMRN","AMRX","AMSC","AMSF","AMST","AMSWA","AMTX","AMWD","AMZD","AMZN","AMZU","AMZZ","ANAB","ANDE","ANEB","ANGH","ANGHW","ANGI","ANGL","ANGO","ANIK","ANIP","ANIX","ANL","ANNX","ANSC","ANSCU","ANSCW","ANSS","ANTE","ANTX","ANY","AOSL","AOTG","AOUT","APA","APCX","APCXW","APDN","APEI","APGE","API","APLD","APLM","APLMW","APLS","APLT","APM","APOG","APP","APPF","APPN","APPS","APRE","APTO","APVO","APWC","APXI","APXIU","APXIW","APYX","AQB","AQMS","AQST","AQU","AQUNR","AQUNU","AQWA","ARAY","ARBB","ARBE","ARBEW","ARBK","ARBKL","ARCB","ARCC","ARCT","ARDX","AREB","AREBW","AREC","ARGX","ARHS","ARKO","ARKOW","ARKR","ARLP","ARM","AROW","ARQ","ARQQ","ARQQW","ARQT","ARRY","ARTL","ARTNA","ARTV","ARTW","ARVN","ARVR","ARWR","ASET","ASLE","ASMB","ASML","ASND","ASNS","ASO","ASPI","ASPS","ASRT","ASRV","ASST","ASTC","ASTE","ASTH","ASTI","ASTL","ASTLW","ASTS","ASTSW","ASUR","ASYS","ATAI","ATAT","ATCOL","ATEC","ATER","ATEX","ATGL","ATHA","ATHE","ATIF","ATLC","ATLCL","ATLCP","ATLCZ","ATLO","ATLX","ATMC","ATMCR","ATMCU","ATMCW","ATMV","ATMVR","ATMVU","ATNF","ATNFW","ATNI","ATOM","ATOS","ATPC","ATRA","ATRC","ATRO","ATSG","ATXG","ATXI","ATXS","ATYR","AUBN","AUDC","AUGX","AUID","AUMI","AUPH","AUR","AURA","AUROW","AUTL","AUUD","AUUDW","AVAH","AVAV","AVBP","AVDL","AVDX","AVGO","AVGR","AVGX","AVIR","AVNW","AVO","AVPT","AVPTW","AVT","AVTE","AVTX","AVXC","AVXL","AWH","AWRE","AXDX","AXGN","AXNX","AXON","AXSM","AXTI","AY","AYRO","AYTU","AZ","AZI","AZN","AZPN","AZTA","BABX","BACK","BAER","BAERW","BAFN","BAND","BANF","BANFP","BANL","BANR","BANX","BAOS","BASE","BATRA","BATRK","BAYA","BAYAR","BAYAU","BBCP","BBGI","BBH","BBIO","BBLG","BBLGW","BBSI","BCAB","BCAL","BCAN","BCAX","BCBP","BCDA","BCG","BCGWW","BCLI","BCML","BCOV","BCOW","BCPC","BCRX","BCSA","BCSAU","BCSAW","BCTX","BCTXW","BCYC","BDGS","BDRX","BDSX","BDTX","BEAM","BEAT","BEATW","BECN","BEEM","BEEZ","BELFA","BELFB","BELT","BENF","BENFW","BETR","BETRW","BFC","BFIN","BFRG","BFRGW","BFRI","BFRIW","BFST","BGC","BGFV","BGLC","BGM","BGNE","BGRN","BGRO","BHAC","BHACU","BHACW","BHAT","BHF","BHFAL","BHFAM","BHFAN","BHFAO","BHFAP","BHIL","BHRB","BIAF","BIAFW","BIB","BIDU","BIGC","BIIB","BILI","BIOA","BIOR","BIOX","BIRD","BIS","BITF","BITS","BIVI","BJDX","BJK","BJRI","BKCH","BKHA","BKHAR","BKHAU","BKIV","BKNG","BKR","BKWO","BKYI","BL","BLAC","BLACR","BLACU","BLACW","BLBD","BLBX","BLCN","BLCR","BLDE","BLDEW","BLDP","BLEU","BLEUR","BLEUU","BLEUW","BLFS","BLFY","BLIN","BLKB","BLLD","BLMN","BLMZ","BLNK","BLRX","BLTE","BLUE","BLZE","BMBL","BMDL","BMEA","BMR","BMRA","BMRC","BMRN","BNAI","BNAIW","BND","BNDW","BNDX","BNGO","BNIX","BNIXR","BNIXW","BNOX","BNR","BNRG","BNTC","BNTX","BNZI","BNZIW","BOCN","BOCNU","BOCNW","BOF","BOKF","BOLD","BOLT","BON","BOOM","BOSC","BOTJ","BOTT","BOTZ","BOWN","BOWNR","BOWNU","BOXL","BPMC","BPOP","BPOPM","BPRN","BPTH","BPYPM","BPYPN","BPYPO","BPYPP","BRAC","BRACR","BRACU","BRAG","BREA","BRFH","BRHY","BRID","BRKH","BRKHU","BRKHW","BRKL","BRKR","BRLS","BRLSW","BRLT","BRNS","BRNY","BROG","BROGW","BRRR","BRTR","BRTX","BRY","BRZE","BSBK","BSCO","BSCP","BSCQ","BSCR","BSCS","BSCT","BSCU","BSCV","BSCW","BSCX","BSCY","BSET","BSFC","BSIIU","BSJO","BSJP","BSJQ","BSJR","BSJS","BSJT","BSJU","BSJV","BSJW","BSLK","BSLKW","BSMO","BSMP","BSMQ","BSMR","BSMS","BSMT","BSMU","BSMV","BSMW","BSMY","BSRR","BSSX","BSVN","BSVO","BSY","BTAI","BTBD","BTBDW","BTBT","BTCS","BTCT","BTCTW","BTDR","BTF","BTFX","BTM","BTMD","BTMWW","BTOC","BTOG","BTSG","BTSGU","BUFC","BUG","BUJA","BUJAR","BUJAU","BUJAW","BULD","BUSE","BVFL","BVS","BWAY","BWB","BWBBP","BWEN","BWFG","BWIN","BWMN","BYFC","BYND","BYNO","BYNOU","BYNOW","BYRN","BYSI","BYU","BZ","BZFD","BZFDW","BZUN","CA","CAAS","CABA","CAC","CACC","CACO","CADL","CAFG","CAKE","CALC","CALM","CALY","CAMT","CAN","CANC","CANQ","CAPNU","CAPR","CAPT","CAPTW","CAR","CARA","CARE","CARG","CARM","CART","CARV","CARZ","CASH","CASI","CASS","CASY","CATH","CATY","CBAN","CBAT","CBFV","CBNK","CBRG","CBRGU","CBRL","CBSH","CBUS","CCAP","CCB","CCBG","CCCC","CCCS","CCD","CCEC","CCEP","CCG","CCGWW","CCIX","CCIXU","CCIXW","CCLD","CCLDO","CCLDP","CCNE","CCNEP","CCNR","CCOI","CCRN","CCSB","CCSI","CCSO","CCTG","CCTS","CCTSU","CCTSW","CDAQ","CDAQU","CDAQW","CDC","CDIO","CDIOW","CDL","CDLX","CDMO","CDNA","CDNS","CDRO","CDROW","CDT","CDTG","CDTTW","CDTX","CDW","CDXC","CDXS","CDZI","CDZIP","CEAD","CEADW","CECO","CEFA","CEG","CELC","CELH","CELU","CELUW","CELZ","CENN","CENT","CENTA","CENX","CEP","CERO","CEROW","CERS","CERT","CETX","CETY","CEVA","CFA","CFB","CFBK","CFFI","CFFN","CFFS","CFFSU","CFFSW","CFLT","CFO","CFSB","CG","CGABL","CGBD","CGBDL","CGBS","CGBSW","CGC","CGEM","CGEN","CGNT","CGNX","CGO","CGON","CGTX","CHCI","CHCO","CHDN","CHEF","CHEK","CHI","CHK","CHKEL","CHKEW","CHKEZ","CHKP","CHMG","CHNR","CHPS","CHR","CHRD","CHRS","CHRW","CHSCL","CHSCM","CHSCN","CHSCO","CHSCP","CHSN","CHTR","CHUY","CHW","CHX","CHY","CIBR","CID","CIFR","CIFRW","CIGI","CIL","CINF","CING","CINGW","CISO","CISS","CITE","CITEU","CITEW","CIVB","CIZ","CJET","CJJD","CKPT","CLAR","CLBK","CLBT","CLDX","CLEU","CLFD","CLGN","CLIR","CLLS","CLMB","CLMT","CLNE","CLNN","CLNNW","CLOA","CLOD","CLOU","CLOV","CLPS","CLPT","CLRB","CLRC","CLRCR","CLRCU","CLRCW","CLRO","CLSD","CLSK","CLSM","CLST","CLWT","CMAX","CMAXW","CMBM","CMCO","CMCSA","CMCT","CME","CMLS","CMMB","CMND","CMPO","CMPOW","CMPR","CMPS","CMPX","CMRX","CMTL","CNCR","CNDT","CNET","CNEY","CNFR","CNFRZ","CNOB","CNOBP","CNSL","CNSP","CNTA","CNTB","CNTM","CNTX","CNTY","CNVS","CNXC","CNXN","COCH","COCHW","COCO","COCP","CODA","CODX","COEP","COEPW","COFS","COGT","COHU","COIN","COKE","COLB","COLL","COLM","COMM","COMT","CONI","CONL","COO","COOP","COOT","COOTW","COPJ","COPP","CORT","CORZ","CORZW","CORZZ","COSM","COST","COWG","COWS","COYA","CPB","CPBI","CPHC","CPIX","CPLS","CPOP","CPRT","CPRX","CPSH","CPSS","CPTN","CPTNW","CPZ","CRAI","CRBP","CRBU","CRCT","CRDF","CRDL","CRDO","CREG","CRESW","CRESY","CREV","CREVW","CREX","CRGO","CRGOW","CRGX","CRIS","CRKN","CRMD","CRML","CRMLW","CRMT","CRNC","CRNT","CRNX","CRON","CROX","CRSP","CRSR","CRTO","CRUS","CRVL","CRVO","CRVS","CRWD","CRWS","CSA","CSB","CSBR","CSCI","CSCO","CSF","CSGP","CSGS","CSIQ","CSLM","CSLMR","CSLMU","CSLMW","CSLR","CSLRW","CSPI","CSQ","CSTE","CSTL","CSWC","CSWCZ","CSWI","CSX","CTAS","CTBI","CTCX","CTCXW","CTEC","CTHR","CTKB","CTLP","CTMX","CTNM","CTNT","CTOR","CTRM","CTRN","CTSH","CTSO","CTXR","CUB","CUBA","CUBWU","CUBWW","CUE","CURI","CURIW","CURR","CUTR","CVAC","CVBF","CVCO","CVGI","CVGW","CVKD","CVLT","CVRX","CVV","CWBC","CWCO","CWD","CWST","CXAI","CXAIW","CXDO","CXSE","CYBR","CYCC","CYCCP","CYCN","CYN","CYRX","CYTH","CYTHW","CYTK","CYTO","CZAR","CZFS","CZNC","CZR","CZWI","DADA","DAIO","DAKT","DALI","DALN","DAPP","DARE","DASH","DATS","DATSW","DAVE","DAVEW","DAWN","DAX","DBGI","DBGIW","DBVT","DBX","DCAP","DCBO","DCGO","DCOM","DCOMG","DCOMP","DCTH","DDI","DDIV","DDOG","DECA","DECAU","DECAW","DECO","DEMZ","DENN","DERM","DFGP","DFGX","DFLI","DFLIW","DGCB","DGHI","DGICA","DGICB","DGII","DGLY","DGRE","DGRS","DGRW","DH","DHAI","DHAIW","DHC","DHCNI","DHCNL","DHIL","DIBS","DIOD","DIST","DISTR","DISTW","DIVD","DJCO","DJT","DJTWW","DKNG","DLHC","DLO","DLPN","DLTH","DLTR","DMAC","DMAT","DMLP","DMRC","DMXF","DNLI","DNTH","DNUT","DOCU","DOGZ","DOMH","DOMO","DOOO","DORM","DOX","DOYU","DPCS","DPCSU","DPCSW","DPRO","DRCT","DRIO","DRIV","DRMA","DRMAW","DRRX","DRS","DRTS","DRTSW","DRUG","DRVN","DSGN","DSGR","DSGX","DSP","DSWL","DSY","DSYWW","DTCK","DTCR","DTI","DTIL","DTSQ","DTSQR","DTSQU","DTSS","DTST","DTSTW","DUET","DUETU","DUETW","DUKH","DUKX","DUO","DUOL","DUOT","DVAL","DVAX","DVLU","DVOL","DVY","DWAS","DWAW","DWSH","DWSN","DWUS","DXCM","DXJS","DXLG","DXPE","DXR","DXYN","DYAI","DYCQ","DYCQR","DYCQU","DYFI","DYN","DYNI","DYTA","EA","EAST","EBAY","EBC","EBIZ","EBMT","EBON","EBTC","ECBK","ECDA","ECDAW","ECOR","ECOW","ECPG","ECX","ECXWW","EDAP","EDBL","EDBLW","EDIT","EDOC","EDRY","EDSA","EDTK","EDUC","EEFT","EEIQ","EEMA","EFAS","EFOI","EFRA","EFSC","EFSCP","EGAN","EGBN","EGHT","EGRX","EH","EHGO","EHLS","EHTH","EJH","EKG","EKSO","ELAB","ELBM","ELDN","ELEV","ELSE","ELTK","ELTX","ELUT","ELVA","ELVN","ELWS","ELYM","EM","EMB","EMBC","EMCB","EMCG","EMCGR","EMCGU","EMCGW","EMEQ","EMIF","EMKR","EML","EMXC","EMXF","ENG","ENGN","ENGNW","ENLT","ENLV","ENPH","ENSC","ENSG","ENTA","ENTG","ENTO","ENTX","ENVB","ENVX","ENZL","EOLS","EOSE","EOSEW","EPIX","EPOW","EPRX","EPSN","EQ","EQIX","EQRR","ERAS","ERET","ERIC","ERIE","ERII","ERNA","ERNZ","ESCA","ESEA","ESGD","ESGE","ESGL","ESGLW","ESGR","ESGRO","ESGRP","ESGU","ESHA","ESHAR","ESLA","ESLAW","ESLT","ESMV","ESOA","ESPO","ESPR","ESQ","ESSA","ESTA","ETEC","ETHA","ETNB","ETON","ETSY","EU","EUDA","EUDAW","EUFN","EURK","EURKR","EURKU","EVAX","EVCM","EVER","EVGN","EVGO","EVGOW","EVGR","EVGRU","EVGRW","EVLV","EVLVW","EVMT","EVO","EVOK","EVRG","EVSD","EVTV","EWBC","EWCZ","EWJV","EWTX","EWZS","EXAI","EXAS","EXC","EXEL","EXFY","EXLS","EXPE","EXPI","EXPO","EXTR","EYE","EYEG","EYEN","EYPT","EZFL","EZGO","EZPW","FA","FAAR","FAAS","FAASW","FAB","FAD","FALN","FAMI","FANG","FANH","FARM","FARO","FAST","FAT","FATBB","FATBP","FATBW","FATE","FBIO","FBIOP","FBIZ","FBL","FBLG","FBNC","FBOT","FBRX","FBYD","FBYDW","FBZ","FCA","FCAL","FCAP","FCBC","FCCO","FCEF","FCEL","FCFS","FCNCA","FCNCO","FCNCP","FCTE","FCUV","FCVT","FDBC","FDCF","FDFF","FDIF","FDIG","FDIV","FDMT","FDNI","FDSB","FDT","FDTS","FDTX","FDUS","FEAM","FEBO","FEIM","FELE","FEM","FEMB","FEMS","FEMY","FENC","FEP","FEPI","FER","FEUZ","FEX","FFBC","FFIC","FFIE","FFIEW","FFIN","FFIV","FFNW","FGBI","FGBIP","FGEN","FGF","FGFPP","FGI","FGIWW","FGM","FHB","FHTX","FIAC","FIACU","FIACW","FIBK","FICS","FID","FINE","FINW","FINX","FIP","FISI","FITB","FITBI","FITBO","FITBP","FIVE","FIVN","FIXD","FIXT","FIZZ","FJP","FKU","FKWL","FLD","FLDB","FLDDU","FLDDW","FLEX","FLGC","FLGT","FLIC","FLL","FLN","FLNC","FLNT","FLUX","FLWS","FLXS","FLYE","FLYW","FMAO","FMB","FMBH","FMED","FMET","FMHI","FMNB","FMST","FMSTW","FNGR","FNK","FNKO","FNLC","FNVT","FNVTU","FNVTW","FNWB","FNWD","FNX","FNY","FOLD","FONR","FORA","FORD","FORL","FORLU","FORLW","FORM","FORR","FORTY","FOSL","FOSLL","FOX","FOXA","FOXF","FOXX","FOXXW","FPA","FPAY","FPXE","FPXI","FRAF","FRBA","FRES","FRGT","FRHC","FRLA","FRLAU","FRLAW","FRME","FRMEP","FROG","FRPH","FRPT","FRSH","FRST","FRSX","FRZA","FSBC","FSBW","FSCS","FSEA","FSFG","FSHP","FSHPR","FSHPU","FSLR","FSTR","FSUN","FSV","FSZ","FTA","FTAG","FTAI","FTAIM","FTAIN","FTAIO","FTAIP","FTC","FTCI","FTCS","FTDR","FTDS","FTEK","FTEL","FTFT","FTGC","FTGS","FTHI","FTHM","FTII","FTIIU","FTIIW","FTLF","FTNT","FTQI","FTRE","FTRI","FTSL","FTSM","FTXG","FTXH","FTXL","FTXN","FTXO","FTXR","FUFU","FUFUW","FULC","FULT","FULTP","FUNC","FUND","FUSB","FUTU","FV","FVC","FVCB","FVNNU","FWONA","FWONK","FWRD","FWRG","FXNC","FYBR","FYC","FYT","FYX","GABC","GAIA","GAIN","GAINL","GAINN","GAINZ","GALT","GAMB","GAME","GAN","GANX","GAQ","GASS","GATE","GATEU","GATEW","GAUZ","GBBK","GBBKR","GBBKW","GBDC","GBIO","GBNY","GCBC","GCMG","GCMGW","GCT","GCTK","GDC","GDEN","GDEV","GDEVW","GDHG","GDRX","GDS","GDST","GDSTR","GDSTU","GDSTW","GDTC","GDYN","GECC","GECCH","GECCI","GECCM","GECCO","GECCZ","GEG","GEGGL","GEHC","GEN","GENE","GENK","GEOS","GERN","GEVO","GFAI","GFAIW","GFGF","GFS","GGAL","GGLL","GGLS","GGR","GGROW","GH","GHIX","GHIXU","GHIXW","GHRS","GHSI","GIFI","GIFT","GIG","GIGGU","GIGGW","GIGM","GIII","GILD","GILT","GINX","GIPR","GIPRW","GLAC","GLACR","GLACU","GLAD","GLADZ","GLBE","GLBS","GLBZ","GLDD","GLDI","GLE","GLLI","GLLIR","GLLIU","GLLIW","GLMD","GLNG","GLOW","GLPG","GLPI","GLRE","GLSI","GLST","GLSTR","GLSTU","GLSTW","GLTO","GLUE","GLXG","GLYC","GMAB","GMGI","GMM","GNFT","GNLN","GNLX","GNMA","GNOM","GNPX","GNSS","GNTA","GNTX","GO","GOCO","GODN","GODNR","GODNU","GOEV","GOEVW","GOGL","GOGO","GOOD","GOODN","GOODO","GOOG","GOOGL","GORV","GOSS","GOVI","GOVX","GOVXW","GP","GPAT","GPATU","GPATW","GPCR","GPIQ","GPIX","GPRE","GPRF","GPRO","GRAB","GRABW","GRAL","GRDI","GRDIW","GREE","GREEL","GRFS","GRI","GRID","GRNQ","GROW","GRPN","GRRR","GRRRW","GRTS","GRVY","GRWG","GRYP","GSBC","GSHD","GSIB","GSIT","GSIW","GSM","GSMGW","GSUN","GT","GTAC","GTACU","GTACW","GTBP","GTEC","GTI","GTIM","GTLB","GTR","GTX","GURE","GUTS","GV","GVH","GVP","GWAV","GWRS","GXAI","GXTG","GYRE","GYRO","HAFC","HAIA","HAIAU","HAIAW","HAIN","HALO","HAO","HAS","HAYN","HBAN","HBANL","HBANM","HBANP","HBCP","HBIO","HBNC","HBT","HCAT","HCKT","HCM","HCOW","HCP","HCSG","HCTI","HCVI","HCVIU","HCVIW","HCWB","HDL","HDSN","HEAR","HECO","HEES","HEJD","HELE","HEPA","HEPS","HERD"};
       List<String> list = new ArrayList<>();
       for(String ticker : tickers){
           list.add(ticker);
       }
       Action action = new PlaceOrderAction(wrapper, null,10000, 10, 10, 5);
       scan.scan(wrapper,action,list);
           //scan.scan(wrapper, new PlaceOrderAction(wrapper, 10000, 10, 10, 5), list);

    }

   // @Test
    public void testVolumeMrkt() throws Exception{
        /*
        TickerReader tickerReader = new TickerReader();
        List<String> tickers = tickerReader.tickers();
        final double percent = 1.2;
        List <String> tickersMetCriteria = new ArrayList<>();
        for(String ticker : tickers) {
            m_client.reqHistoricalData(1000 + 10, new USStockContract(ticker), "", "200 D", "1 day", "TRADES", 1, 1, false, null);
            Thread.sleep(1000);
            Calculator calculator = new VolumeCalculator();
            double averageVolume = calculator.calculate(wrapper.getList());
            List<Bar> list = wrapper.getList();

            double lastVolume = list.get(list.size() - 1).volume().longValue();
            if (lastVolume > percent * averageVolume) {
                tickersMetCriteria.add(ticker);
               // System.out.println("Last volume is ===============" + lastVolume);
            }

        }
        System.out.println(tickersMetCriteria);
        //calculator.calculate(wrapper.getList());
      //  SMACalculator smaCalculator = new SMACalculator();
        //smaCalculator.calculate(wrapper.getList(), 200);
        // System.out.printf("TESTTT#######@@@@@%s%n", );

         */
    }


    //@Test
    public void testPlaceOrder() throws InterruptedException {

        double accountValue= 10000;
        int risk = 1;
        double amountToPut = accountValue/100 * risk;
        double price =191.25;
        double stopPrice= price - price/100* 4;
        Contract contract = new USStockContract("AMZN");
        Order order = new Order();
        int orderInitId=9999239;
        wrapper.nextValidId(orderInitId);
        int orderId = wrapper.getCurrentOrderId();
        orderId++;
        order.action("BUY");
        order.orderType("MKT");
        order.totalQuantity(Decimal.get(1.0));
        m_client.placeOrder(orderId, contract, order);
        Thread.sleep(2000);
        wrapper.nextValidId(orderId);
        orderId=wrapper.getCurrentOrderId();
        orderId++;
        order.action("SELL");
        order.orderType("LMT");
        order.totalQuantity(Decimal.get(1.0));
        order.lmtPrice(stopPrice);
        m_client.placeOrder(orderId, contract, order);
        Thread.sleep(2000);


        /*
        double accountValue = 10000;
        int risk = 1;
        double amountToPut = accountValue / 100 * risk;

// Create contract for Amazon stock
        Contract contract = new USStockContract("AMZN");

// Create an order
        Order order = new Order();
        order.action("SELL");
        order.orderType("LMT");
        order.totalQuantity(Decimal.get(1.0));

// Get the next valid order ID
        wrapper.nextValidId(654500); // This method will asynchronously return the next valid ID
        int orderId = wrapper.getCurrentOrderId(); // Get the order ID provided by nextValidId()
        orderId++; // Increment it to ensure uniqueness
        orderId++;

// Use the dynamic order ID for placing the order
        m_client.placeOrder(orderId, contract, order); // Use the unique order ID

        Thread.sleep(3000); // Optional: Delay to simulate waiting for order submission or response
        wrapper.nextValidId(orderId);

         */

    }




}

class USStockContract extends Contract {
    public USStockContract(String symbol)
    {
        super.symbol(symbol);
        super.secType("STK");
        super.exchange("SMART");
        super.primaryExch("ISLAND");
        super.currency("USD");
    }
}

