package com.mr.data.common.util.cutImage;

import com.mr.data.common.util.BaiduOCRUtil;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.List;

public class OpenCVTest {
	public static void main(String[] args) {
        /*// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
         System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

         String dest = "C:\\Users\\Space\\Desktop";

        File imgFile = new File(dest+"/test.jpg");

        Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);

        Mat dst = new Mat();

        Imgproc.adaptiveThreshold(src, dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 13, 5);
        Imgcodecs.imwrite(dest + "/AdaptiveThreshold" + imgFile.getName(), dst);*/
		new OpenCVTest().cutImgYTest();
	}

	public void toGray() {
		// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String dest = "C:\\Users\\Space\\Desktop";

		File imgFile = new File(dest+"/test.jpg");

		//方式一
		Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		//保存灰度化的图片
		Imgcodecs.imwrite(dest + "/toGray" + imgFile.getName(), src);
	}

	public void toGray2() {
		// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String dest = "C:\\Users\\Space\\Desktop";
		File imgFile = new File(dest+"/444.jpg");

		//方式二
		Mat src = Imgcodecs.imread(imgFile.toString());
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		src = gray;
		//保存灰度化的图片
		Imgcodecs.imwrite(dest + "/toGray2" + imgFile.getName(), src);
	}

	public Mat binaryzation(Mat mat) {
		int BLACK = 0;
		int WHITE = 255;
		int ucThre = 0, ucThre_new = 127;
		int nBack_count, nData_count;
		int nBack_sum, nData_sum;
		int nValue;
		int i, j;

		int width = mat.width(), height = mat.height();
		//寻找最佳的阙值
		while (ucThre != ucThre_new) {
			nBack_sum = nData_sum = 0;
			nBack_count = nData_count = 0;

			for (j = 0; j < height; ++j) {
				for (i = 0; i < width; i++) {
					nValue = (int) mat.get(j, i)[0];

					if (nValue > ucThre_new) {
						nBack_sum += nValue;
						nBack_count++;
					} else {
						nData_sum += nValue;
						nData_count++;
					}
				}
			}

			nBack_sum = nBack_sum / nBack_count;
			nData_sum = nData_sum / nData_count;
			ucThre = ucThre_new;
			ucThre_new = (nBack_sum + nData_sum) / 2;
		}

		//二值化处理
		int nBlack = 0;
		int nWhite = 0;
		for (j = 0; j < height; ++j) {
			for (i = 0; i < width; ++i) {
				nValue = (int) mat.get(j, i)[0];
				if (nValue > ucThre_new) {
					mat.put(j, i, WHITE);
					nWhite++;
				} else {
					mat.put(j, i, BLACK);
					nBlack++;
				}
			}
		}

		// 确保白底黑字
		if (nBlack > nWhite) {
			for (j = 0; j < height; ++j) {
				for (i = 0; i < width; ++i) {
					nValue = (int) (mat.get(j, i)[0]);
					if (nValue == 0) {
						mat.put(j, i, WHITE);
					} else {
						mat.put(j, i, BLACK);
					}
				}
			}
		}
		return mat;
	}
	//测试二值化
	public void binaryzation() {
		// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String dest = "C:\\Users\\Space\\Desktop";
		File imgFile = new File(dest+"/333.jpg");
		//先经过一步灰度化
		Mat src = Imgcodecs.imread(imgFile.toString());
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		src = gray;
		//二值化
		binaryzation(src);
		Imgcodecs.imwrite(dest + "/binaryzation" + imgFile.getName(), src);
	}
	public void cutImgYTest(){
		StringBuffer stringBuffer = new StringBuffer("");
		// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String dest = "C:\\Users\\Space\\Desktop/binaryzation111.jpg";
		ImageUtils imageUtil = new ImageUtils();
		imageUtil.loadImg(dest);
		List<Mat> matListY = imageUtil.cutImgX();

		int i = 0;
		for(Mat matY : matListY){
			// 亮度降低
			Mat dst = new Mat();
			Mat black = Mat.zeros(matY.size(), matY.type());
			Core.addWeighted(matY, 0.5, black, 0.5, 0, dst);
			stringBuffer.append(cutImgXTest(dst,i));
			i++;
		}

		System.out.println(stringBuffer.toString());
	}
	public String cutImgXTest(Mat mat,int j){
		StringBuffer stringBuffer = new StringBuffer("");
		//腾讯
		/*AIOCRUtil aiocrUtil = new AIOCRUtil();
		aiocrUtil.setTaipAppId("2107602349");
		aiocrUtil.setTaipAppKey("ft510AJ3xrB5pfK9");*/

		BaiduOCRUtil baiduOCRUtil = new BaiduOCRUtil();
		baiduOCRUtil.setAppId("11641746");
		baiduOCRUtil.setAppKey("amGikVFG3DMxS70DorUNUlvR");
		baiduOCRUtil.setSecretKey("1Ze4bta22sW70V5bvlexbnB8b5kbNXqG");

		ImageUtils imageUtil = new ImageUtils();
		imageUtil.ImageUtils(mat);
		List<Mat> matListX = imageUtil.cutImgY();
		int i = 0;
		for(Mat matX : matListX){
			// 亮度提升
			Mat dst = new Mat();
			Mat black = Mat.zeros(matX.size(), matX.type());
			Core.addWeighted(matX, 2, black, 0.1, 0, dst);
			String fileName = "C:\\Users\\Space\\Desktop/xy/binaryzation111_"+j+"_"+i+".jpg";
			Imgcodecs.imwrite(fileName, dst);
			//百度
			String strBaidu = BaiduOCRUtil.getTextStrFromImageFile(fileName,"");
			//腾讯
			//String taiP=AIOCRUtil.getTextFromImageFile(fileName);

			stringBuffer.append(strBaidu.replaceAll("[\\s]{0,}","")).append(" ");
			i++;
		}
		return stringBuffer.toString();


	}
}
