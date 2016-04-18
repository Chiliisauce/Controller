//package com.example.dabai.controller.model;
//
//import android.content.Context;
//
//import com.ftdi.j2xx.D2xxManager;
//import com.ftdi.j2xx.FT_Device;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//
///**
// * Created by cfc3434 on 2016/1/4.
// */
//public class Device {
//    static Context deviceContext;
//
//    protected final int readLength = 512;
//    protected int openIndex = 0;
//    protected String devDescription  = null;
//    protected int devCount = 0;
//    protected D2xxManager ftD2xx= null;
//    protected FT_Device ftDev = null;
//    protected int baudrate = 9600;
//    protected byte[] txData;
//    protected byte[] rxData = new byte[readLength];
//
//    protected boolean configured = false;
//    protected boolean readDataGoing = false;
//
//    public Device(Context parentContext){
//        deviceContext = parentContext;
//    }
//
//    public int scanDevice(){
//        try {
//            ftD2xx = D2xxManager.getInstance(deviceContext);
//            devCount = ftD2xx.createDeviceInfoList(deviceContext);
//        } catch(D2xxManager.D2xxException ex){
//            System.out.println("No device");
//        }
//        return devCount;
//    }
//
//    public ArrayList<String> getDevicesInfo(){
//        ArrayList<String> devicesInfoList = new ArrayList<String>();
//        if (ftD2xx != null){
//            for (int i = 0; i < devCount; i++) {
//                devicesInfoList.add(ftD2xx.getDeviceInfoListDetail(i).description);
//            }
//        }
//
//        return devicesInfoList;
//    }
//
//    public void setOpenIndex (int index){
//        openIndex = index;
//    }
//
//    public void setDevDescription (String devDescription){
//        this.devDescription = devDescription;
//    }
//
//    public void setBaudRate (int rate){
//        baudrate = rate;
//    }
//
//    public int openDeviceByIndex(){
//        if (ftD2xx != null  && openIndex != 0) {
//            if (ftDev == null) {
//                ftDev = ftD2xx.openByIndex(deviceContext, openIndex);
//            }
//            else{
//                synchronized(ftDev) {
//                    ftDev = ftD2xx.openByIndex(deviceContext, openIndex);
//                }
//            }
//            configured = false;
//            return 1;
//        }
//        else{
//            return 0;
//        }
//    }
//
//    public int openDeviceByDescription(){
//        if (ftD2xx != null  && (openIndex != 0 || devDescription != null)) {
//            if (ftDev == null) {
//                ftDev = ftD2xx.openByDescription(deviceContext, devDescription);
//            }
//            else{
//                synchronized(ftDev) {
//                    ftDev = ftD2xx.openByDescription(deviceContext, devDescription);
//                }
//            }
//            configured = false;
//            return 1;
//        }
//        else{
//            return 0;
//        }
//    }
//
//    public void closeDevice() {
//        if(ftDev != null)
//        {
//            synchronized(ftDev)
//            {
//                if( true == ftDev.isOpen())
//                {
//                    ftDev.close();
//                }
//            }
//        }
//    }
//
//    public int configDevice(){
//        if (ftDev == null || ftDev.isOpen() == true) {
//            // Reset FT Device
//            ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
//            // Set Baud Rate
//            ftDev.setBaudRate(baudrate);
//            // Set Data Bit , Stop Bit , Parity Bit
//            ftDev.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
//            // Set Flow Control
//            ftDev.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x0b, (byte) 0x0d);
//
//            configured = true;
//
//            return 1;
//        }
//        else{
//            return 0;
//        }
//    }
//
//    public void setTxData(byte[] data){
//        this.txData = data;
//    }
//
//    public int sendData() {
//        if (ftDev == null || ftDev.isOpen() == false || configured == false) {
//            return 0;
//        }
//
//        ftDev.setLatencyTimer((byte) 16);
//        ftDev.write(txData);
//        return 1;
//    }
//
//    public int readData() {
//        if (ftDev == null || ftDev.isOpen() == false || configured == false) {
//            return 0;
//        }
//
//        int available = ftDev.getQueueStatus();
//        if (available > 0) {
//            // Make sure available data is not too much
//            if (available > readLength) {
//                available = readLength;
//            }
//            //System.out.println("Available:" + available);
//            ftDev.read(rxData, available);
//            return 1;
//        }
//
//        return 0;
//    }
//
//    public int startReadingData(){
//        if (ftDev == null ||  ftDev.isOpen() == false || configured == false) {
//            return 0;
//        }
//        ftDev.purge(D2xxManager.FT_PURGE_TX);
//        ftDev.restartInTask();
//        this.readDataGoing = true;
//        return 1;
//    }
//
//    public int stopReadingData(){
//        if (ftDev == null || ftDev.isOpen() == false || configured == false || readDataGoing == false) {
//            return 0;
//        }
//        ftDev.stopInTask();
//        this.readDataGoing = false;
//        return 1;
//    }
//
//    public byte[] getRxData() {
//        return rxData;
//    }
//
//    public byte[] getTxData() {
//        return txData;
//    }
//
//    public String getRxDataAsText() {
//        String str = null;
//        try {
//            if(rxData != null) {
//                str = new String(rxData, "UTF-8");
//            }
//        } catch (UnsupportedEncodingException e){
//            System.out.println("Unsupported encoding");
//        }
//        return str;
//    }
//    public boolean getReadDataGoing(){
//        return this.readDataGoing;
//    }
//
//    public FT_Device getFtDev(){
//        return ftDev;
//    }
//
//    public void disconnectDevice(){
//        stopReadingData();
//        try {
//            // Wait for thread to end
//            Thread.sleep(50);
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        if(ftDev != null)
//        {
//            synchronized(ftDev)
//            {
//                if(ftDev.isOpen() == true)
//                {
//                    ftDev.close();
//                }
//            }
//        }
//    }
//}
