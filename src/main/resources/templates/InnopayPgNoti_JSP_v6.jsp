<%@ page  contentType="text/html; charset=euc-kr" %>
<%@ page import = "java.io.*" %>
<%@ page import = "java.util.Calendar" %>
<%
/*******************************************************************************
 * FILE NAME : InnopayPgNoti_JSP.jsp
 * DATE : 2015.03.18
*******************************************************************************/

/*******************************************************************************
 * ������           �ѱ۸�
 *--------------------------------------------------------------------------------
 ********************************************************************************
 * ����
 ********************************************************************************
 * transSeq			�ŷ���ȣ
 * userId			����ھ��̵�
 * userName			������̸�
 * userPhoneNo		������޴�����ȣ
 * moid				�ֹ���ȣ
 * goodsName		��ǰ��
 * goodsAmt			��ǰ�ݾ�
 * buyerName		�����ڸ�
 * buyerPhoneNo		�������޴�����ȣ
 * pgCode			PG�ڵ� ( 01:NICE / 02:KICC / 03:INFINISOFT / 04:KSNET / 05:KCP / 06:SMATRO )
 * pgName			PG��
 * payMethod		��������( 01:���ݰ��� / 02:�ſ�ī�� / 03:�ſ�ī��ARS )
 * payMethodName	�������ܸ�
 * pgMid			PG���̵�
 * pgSid			PG���񽺾��̵�
 * status			�ŷ����� ( 25:�����Ϸ� / 85:������� )
 * statusName		�ŷ����¸�
 * pgResultCode		PG����ڵ�
 * pgResultMsg		PG����޼���
 * pgAppDate		PG��������
 * pgAppTime		PG���νð�
 * pgTid			PG�ŷ���ȣ
 * approvalAmt		���αݾ�
 * approvalNo		���ι�ȣ
 ********************************************************************************
 * ���۰���(���ݿ�����)
 ********************************************************************************
 * cashReceiptType			�������� ( 1:�ҵ���� / 2:�������� )
 * cashReceiptTypeName		�������и�
 * cashReceiptSupplyAmt		���ް�
 * cashReceiptVat			�ΰ���
 ********************************************************************************
 * �ſ�ī�����
 ********************************************************************************
 * cardNo					ī���ȣ
 * cardQuota				�Һΰ���
 * cardIssueCode			�߱޻��ڵ� ( �޴������� )
 * cardIssueName			�߱޻��
 * cardAcquireCode			���Ի��ڵ� ( �޴������� )
 * cardAcquireName			���Ի��
 ********************************************************************************
 * �������
 ********************************************************************************
 * cancelAmt				��ҿ�û�ݾ�
 * cancelMsg				��ҿ�û�޼���
 * cancelResultCode			��Ұ���ڵ�
 * cancelResultMsg			��Ұ���޼���
 * cancelAppDate			��ҽ�������
 * cancelAppTime			��ҽ��νð�
 * cancelPgTid				PG�ŷ���ȣ
 * cancelApprovalAmt		���αݾ�
 * cancelApprovalNo			���ι�ȣ
  ********************************************************************************
 * �ڵ�����
 ********************************************************************************
 * billKey					�߱޹��� ��Ű
*******************************************************************************/

/***********************************************************************************
 * ���Ǵϼ���Ʈ�� �����ϴ� ARS ���� ����� �����Ͽ� DB ó�� �ϴ� �κ� �Դϴ�.	
 * �ʿ��� �Ķ���Ϳ� ���� DB �۾��� �����Ͻʽÿ�.
 ***********************************************************************************/	
		
/**********************************************************************************
    �̺κп� �α����� ��θ� �������ּ���.*/
		String log_path = "c:/infinisoft";
/**********************************************************************************/
		String add = request.getRemoteAddr().toString();
		
		
		transSeq     	= request.getParameter("transSeq");
		userId       	= request.getParameter("userId");
		userName		= request.getParameter("userName");
		userPhoneNo     = request.getParameter("userPhoneNo");
		moid        	= request.getParameter("moid");
		goodsName   	= request.getParameter("goodsName");
		goodsAmt   		= request.getParameter("goodsAmt");
		buyerName  		= request.getParameter("buyerName");
		buyerPhoneNo   	= request.getParameter("buyerPhoneNo");
		pgCode    		= request.getParameter("pgCode");
		pgName  		= request.getParameter("pgName");
		payMethod   	= request.getParameter("payMethod");
		payMethodName	= request.getParameter("payMethodName");
		pgMid   		= request.getParameter("pgMid");
		pgSid			= request.getParameter("pgSid");
		status    		= request.getParameter("status");
		statusName		= request.getParameter("statusName");
		pgResultCode   	= request.getParameter("pgResultCode");
		pgResultMsg   	= request.getParameter("pgResultMsg");
		pgAppDate     	= request.getParameter("pgAppDate");
		pgAppTime     	= request.getParameter("pgAppTime");
		pgTid			= request.getParameter("pgTid");
		approvalAmt		= request.getParameter("approvalAmt");
		approvalNo		= request.getParameter("approvalNo");
		
		if("01".equals(payMethod)){
			//���ݰ���(���ݿ�����)
			cashReceiptType			= request.getParameter("cashReceiptType");
			cashReceiptTypeName		= request.getParameter("cashReceiptTypeName");
			cashReceiptSupplyAmt	= request.getParameter("cashReceiptSupplyAmt");
			cashReceiptVat			= request.getParameter("cashReceiptVat");

		}else if("02".equals(payMethod) || "03".equals(payMethod)){
			//�ſ�ī�� & �ſ�ī��ARS
			cardNo				= request.getParameter("cardNo");
			cardQuota			= request.getParameter("cardQuota");
			cardIssueCode		= request.getParameter("cardIssueCode");
			cardIssueName		= request.getParameter("cardIssueName");
			cardAcquireCode		= request.getParameter("cardAcquireCode");
			cardAcquireName		= request.getParameter("cardAcquireName");
		}else if("09".equals(payMethod)){
			//�ڵ�����
			billKey				= request.getParameter("billKey");
		}


		if("85".equals(status)){
			//�������
			cancelAmt			= request.getParameter("cancelAmt");
			cancelMsg			= request.getParameter("cancelMsg");
			cancelResultCode	= request.getParameter("cancelResultCode");
			cancelResultMsg		= request.getParameter("cancelResultMsg");
			cancelAppDate		= request.getParameter("cancelAppDate");
			cancelAppTime		= request.getParameter("cancelAppTime");
			cancelPgTid			= request.getParameter("cancelPgTid");
			cancelApprovalAmt	= request.getParameter("cancelApprovalAmt");
			cancelApprovalNo	= request.getParameter("cancelApprovalNo");
		}
		
		//��ǰ ������ �߰��� ��� (�ּ�����)
		//goodsSize				= request.getParameter("goodsSize");
		//goodsCodeArray		= request.getParameter("goodsCodeArray");
		//goodsNameArray		= request.getParameter("goodsNameArray");
		//goodsAmtArray			= request.getParameter("goodsAmtArray");
		//goodsCntArray			= request.getParameter("goodsCntArray");
		//totalAmtArray			= request.getParameter("totalAmtArray");

		//����� ������ �߰��� ��� (�ּ�����)
		//zoneCode				= request.getParameter("zoneCode");
		//address				= request.getParameter("address");
		//addressDetail			= request.getParameter("addressDetail");
		//recipientName			= request.getParameter("recipientName");
		//recipientPhoneNo		= request.getParameter("recipientPhoneNo");
		//comment				= request.getParameter("comment");
		
		try
		{
			writeLog(log_path);

//***********************************************************************************
//
//	������ ���� �����ͺ��̽��� ��� ���������� ���� �����ÿ��� "OK"�� ���ǴϷ�
//	�����ϼž��մϴ�. �Ʒ� ���ǿ� �����ͺ��̽� ������ �޴� FLAG ������ ��������
//	(����) OK�� �������� �����ø� ���Ǵ� ���� ������ "OK"�� �����Ҷ����� ��� ������(������ �ִ�Ƚ������)�� �õ��մϴ�
//	��Ÿ �ٸ� ������ out.println(response.write)�� ���� �����ñ� �ٶ��ϴ�
//	if (�����ͺ��̽� ��� ���� ���� ���Ǻ��� = true) 
//  {
    	out.print("0000"); // ����� ������ ������
//  }
		}
		catch(Exception e)
		{
			out.print(e.getMessage());
		}
%>
<%!

	private String transSeq   			;
	private String userId     			;
	private String userName	 			;
	private String userPhoneNo			;
	private String moid      			;
	private String goodsName 			;
	private String goodsAmt 			;
	private String buyerName			;
	private String buyerPhoneNo 		;
	private String pgCode  				;
	private String pgName				;
	private String payMethod 			;
	private String payMethodName		;
	private String pgMid 				;
	private String pgSid				;
	private String status  				;
	private String statusName  			;
	private String pgResultCode 		;
	private String pgResultMsg 			;
	private String pgAppDate   			;
	private String pgAppTime   			;
	private String pgTid 				;
	private String approvalAmt 			;
	private String approvalNo 			;
	
	//���ݰ���(���ݿ�����)
	private String cashReceiptType 		;
	private String cashReceiptTypeName 	;
	private String cashReceiptSupplyAmt	;
	private String cashReceiptVat 		;
	
	//�ſ�ī�� & �ſ�ī��ARS
	private String cardNo 				;
	private String cardQuota 			;
	private String cardIssueCode 		;
	private String cardIssueName 		;
	private String cardAcquireCode 		;
	private String cardAcquireName 		;
	
	//�������
	private String cancelAmt 			;
	private String cancelMsg 			;
	private String cancelResultCode 	;
	private String cancelResultMsg 		;
	private String cancelAppDate 		;
	private String cancelAppTime 		;
	private String cancelPgTid 			;
	private String cancelApprovalAmt 	;
	private String cancelApprovalNo 	;

	//�ڵ�����
	private String billKey 				;
	
	//��ǰ ������ �߰��� ��� (�ּ�����)
	//private String goodsSize			;
	//private String goodsCodeArray		;
	//private String goodsNameArray		;
	//private String goodsAmtArray		;
	//private String goodsCntArray		;
	//private String totalAmtArray		;

	//����� ������ �߰��� ��� (�ּ�����)
	//private String zoneCode			;
	//private String address			;
	//private String addressDetail		;
	//private String recipientName		;
	//private String recipientPhoneNo	;
	//private String comment			;
	
	private StringBuffer times			;
    private String getDate()
    {
    	Calendar calendar = Calendar.getInstance();
    	
    	times = new StringBuffer();
        times.append(Integer.toString(calendar.get(Calendar.YEAR)));
		if((calendar.get(Calendar.MONTH)+1)<10)
        { 
            times.append("0"); 
        }
		times.append(Integer.toString(calendar.get(Calendar.MONTH)+1));
		if((calendar.get(Calendar.DATE))<10) 
        { 
            times.append("0");	
        } 
		times.append(Integer.toString(calendar.get(Calendar.DATE)));
    	
    	return times.toString();
    }
    
    private String getTime()
    {
    	Calendar calendar = Calendar.getInstance();
    	
    	times = new StringBuffer();

    	times.append("[");
    	if((calendar.get(Calendar.HOUR_OF_DAY))<10) 
        { 
            times.append("0"); 
        } 
 		times.append(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
 		times.append(":");
 		if((calendar.get(Calendar.MINUTE))<10) 
        { 
            times.append("0"); 
        }
 		times.append(Integer.toString(calendar.get(Calendar.MINUTE)));
 		times.append(":");
 		if((calendar.get(Calendar.SECOND))<10) 
        { 
            times.append("0"); 
        }
 		times.append(Integer.toString(calendar.get(Calendar.SECOND)));
 		times.append("]");
 		
 		return times.toString();
    }

    private void writeLog(String log_path) throws Exception
    {

        File file = new File(log_path);
        file.createNewFile();

        FileWriter file2 = new FileWriter(log_path+"/infini_ars_recv_"+getDate()+".log", true);


		file2.write("\n************************************************\n");
		file2.write("PageCall time    : " + getTime());
		file2.write("\ntransSeq       : " + transSeq   ); 
		file2.write("\nuserId         : " + userId     );
		file2.write("\nuserName       : " + userName    ); 
		file2.write("\nuserPhoneNo    : " + userPhoneNo     ); 
		file2.write("\nmoid           : " + moid      );
		file2.write("\ngoodsName      : " + goodsName );
		file2.write("\ngoodsAmt       : " + goodsAmt );
		file2.write("\nbuyerName      : " + buyerName); 
		file2.write("\nbuyerPhoneNo   : " + buyerPhoneNo ); 
		file2.write("\npgCode         : " + pgCode  ); 
		file2.write("\npgName         : " + pgName); 
		file2.write("\npayMethod      : " + payMethod ); 
		file2.write("\npayMethodName  : " + payMethodName    ); 
		file2.write("\npgMid          : " + pgMid );
		file2.write("\npgSid          : " + pgSid );
		file2.write("\nstatus         : " + status  ); 
		file2.write("\nstatusName     : " + statusName  ); 
		file2.write("\npgResultCode   : " + pgResultCode ); 
		file2.write("\npgResultMsg    : " + pgResultMsg ); 
		file2.write("\npgAppDate      : " + pgAppDate   ); 
		file2.write("\npgAppTime      : " + pgAppTime   );  
		file2.write("\npgTid          : " + pgTid   );
		file2.write("\napprovalAmt    : " + approvalAmt   );
		file2.write("\napprovalNo     : " + approvalNo   );
		
		
		if("01".equals(payMethod)){
			//���ݰ���(���ݿ�����)
			file2.write("\ncashReceiptType     	: " + cashReceiptType   );
			file2.write("\ncashReceiptTypeName  : " + cashReceiptTypeName   );
			file2.write("\ncashReceiptSupplyAmt	: " + cashReceiptSupplyAmt   );
			file2.write("\ncashReceiptVat     	: " + cashReceiptVat   );
			
		}else if("02".equals(payMethod) || "03".equals(payMethod)){
			//�ſ�ī�� & �ſ�ī��ARS
			file2.write("\ncardNo     			: " + cardNo   );
			file2.write("\ncardQuota     		: " + cardQuota   );
			file2.write("\ncardIssueCode     	: " + cardIssueCode   );
			file2.write("\ncardIssueName     	: " + cardIssueName   );
			file2.write("\ncardAcquireCode     	: " + cardAcquireCode   );
			file2.write("\ncardAcquireName     	: " + cardAcquireName   );
		}else if("09".equals(payMethod)){
			//�ڵ�����
			file2.write("\nbillKey     			: " + billKey   );
		}


		if("85".equals(status)){
			//�������
			file2.write("\ncancelAmt     		: " + cancelAmt   );
			file2.write("\ncancelMsg     		: " + cancelMsg   );
			file2.write("\ncancelResultCode     : " + cancelResultCode   );
			file2.write("\ncancelResultMsg     	: " + cancelResultMsg   );
			file2.write("\ncancelAppDate     	: " + cancelAppDate   );
			file2.write("\ncancelAppTime     	: " + cancelAppTime   );
			file2.write("\ncancelPgTid     		: " + cancelPgTid   );
			file2.write("\ncancelApprovalAmt    : " + cancelApprovalAmt   );
			file2.write("\ncancelApprovalNo     : " + cancelApprovalNo   );
		}
		
		//��ǰ ������ �߰��� ��� (�ּ�����)
		//file2.write("\ngoodsSize     			: " + goodsSize);
		//file2.write("\ngoodsCodeArray     	: " + goodsCodeArray);
		//file2.write("\ngoodsNameArray     	: " + goodsNameArray);
		//file2.write("\ngoodsAmtArray     		: " + goodsAmtArray);
		//file2.write("\ngoodsCntArray     		: " + goodsCntArray);
		//file2.write("\ntotalAmtArray     		: " + totalAmtArray	);

		//����� ������ �߰��� ��� (�ּ�����)
		//file2.write("\nzoneCode     			: " + zoneCode);
		//file2.write("\naddress     			: " + address);
		//file2.write("\naddressDetail     		: " + addressDetail);
		//file2.write("\nrecipientName     		: " + recipientName);
		//file2.write("\nrecipientPhoneNo     	: " + recipientPhoneNo);
		//file2.write("\ncomment     			: " + comment);
		
		file2.write("\n************************************************\n");

        file2.close();

    }
%>
