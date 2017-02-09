package com.sunrise.lovesunrise.util.aqi;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;

public class AirInterface
{

  private static final String NAMESPACE = "http://tempuri.org/";
  private static final String URL = "http://mobile.bjmemc.com.cn/AirService/Service.asmx";

  public String HttpSoap(String paramString)
  {
    try
    {
      Element[] localObject1 = new Element[1];
      localObject1[0] = new Element().createElement(NAMESPACE, "MySoapHeader");
      Object localObject2 = new Element().createElement(NAMESPACE, "UserName");
      ((Element)localObject2).addChild(4, "beijingaqi");
      localObject1[0].addChild(2, localObject2);
      localObject2 = new Element().createElement(NAMESPACE, "PassWord");
      ((Element)localObject2).addChild(4, "bjaqi2012pds");
      localObject1[0].addChild(2, localObject2);
      SoapObject localSoapObject = new SoapObject(NAMESPACE, paramString);
      localObject2 = new SoapSerializationEnvelope(SoapEnvelope.VER12);
      ((SoapSerializationEnvelope)localObject2).headerOut = localObject1;
      ((SoapSerializationEnvelope)localObject2).bodyOut = localSoapObject;
      HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
      httpTransportSE.debug = true;
      httpTransportSE.call(NAMESPACE + paramString, (SoapEnvelope)localObject2);
      paramString = ((SoapSerializationEnvelope)localObject2).getResponse().toString();
      return paramString;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return "error";
  }

  public String HttpSoap(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      Element[] arrayOfElement = new Element[1];
      arrayOfElement[0] = new Element().createElement(NAMESPACE, "MySoapHeader");
      Object localObject = new Element().createElement(NAMESPACE, "UserName");
      ((Element)localObject).addChild(4, "beijingaqi");
      arrayOfElement[0].addChild(2, localObject);
      localObject = new Element().createElement(NAMESPACE, "PassWord");
      ((Element)localObject).addChild(4, "bjaqi2012pds");
      arrayOfElement[0].addChild(2, localObject);
      localObject = new SoapObject(NAMESPACE, paramString1);
      ((SoapObject)localObject).addProperty("DevID", paramString2);
      ((SoapObject)localObject).addProperty("DevType", paramString3);
      SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
      envelope.dotNet = true;
      envelope.headerOut = arrayOfElement;
      envelope.bodyOut = localObject;
      HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
      httpTransportSE.debug = true;
      httpTransportSE.call(NAMESPACE + paramString1, envelope);
      paramString1 = envelope.getResponse().toString();
      return paramString1;
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    return "error";
  }

  public String GetPredict()
  {
    return HttpSoap("GetPredict");
  }

  public String getAlert()
  {
    return HttpSoap("GetAlert");
  }

  public String getBackground()
  {
    return HttpSoap("GetBackground");
  }

  public String getData(String paramString1, String paramString2)
  {
    return HttpSoap("GetData", paramString1, paramString2);
  }

  public String getMessage()
  {
    return HttpSoap("GetMessage");
  }

  public String getVer()
  {
    return HttpSoap("GetVer");
  }
}