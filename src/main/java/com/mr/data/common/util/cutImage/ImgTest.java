package com.mr.data.common.util.cutImage;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.*;

public class ImgTest {
    static {
        System.load("F:\\20171010微策战团\\Codelibaray\\mr-data-factory-phase3\\mr-data-factory-phase3\\src\\main\\libs/opencv_java341.dll");
    }
    public static void main(String[] args){

        String basePath ="C:\\Users\\Space\\Desktop\\xy\\";
        String fileName = "222.jpg";
        String fileTemp ="C:\\Users\\Space\\Desktop\\xy\\temp\\";
        new ImgTest().imgCut(basePath,fileName, fileTemp);
    }

    public void imgCut(String basePath,String fileName,String fileTemp){
        //二值化
        binaryzation(basePath,fileName);

        long startTime = System.currentTimeMillis();

        File dir = new File(fileTemp);
        if(!dir.exists()){
            dir.mkdir();
        }
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                f.delete();
            }
        }
        System.out.println(basePath+fileName);
        /*******************************************/
        Mat src = Imgcodecs.imread(basePath+fileName);
        if (src.empty()) {
            System.out.println("not found file");
            return;
        }

        Mat gray = new Mat();
        Mat erod = new Mat();
        Mat blur = new Mat();
        int src_height = src.cols(), src_width = src.rows();
        // 先转为灰度 cvtColor(src, gray, COLOR_BGR2GRAY);
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);

        /**
         * 腐蚀（黑色区域变大） Mat element = getStructuringElement(MORPH_RECT,
         * Size(erodeSize, erodeSize)); erode(gray, erod, element);
         */
        int erodeSize = src_height / 200;
        if (erodeSize % 2 == 0) {
            erodeSize++;
        }
        Mat element = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(erodeSize, erodeSize));
        Imgproc.erode(gray, erod, element);

        // 高斯模糊化
        int blurSize = src_height / 200;
        if (blurSize % 2 == 0) {
            blurSize++;
        }
        Imgproc.GaussianBlur(erod, blur, new Size(blurSize, blurSize), 0, 0);

        // 封装的二值化 adaptiveThreshold(~gray, thresh, 255,
        // CV_ADAPTIVE_THRESH_MEAN_C, CV_THRESH_BINARY, 15, -2);
        Mat thresh = gray.clone();
        Mat xx = new Mat();
        Core.bitwise_not(gray, xx);// 反色
        Imgproc.adaptiveThreshold(xx, thresh, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);
		/*
		 * 这部分的思想是将线条从横纵的方向处理后抽取出来，再进行交叉，矩形的点，进而找到矩形区域的过程
		 *
		 */
        // Create the images that will use to extract the horizonta and vertical
        // lines
        // 使用二值化后的图像来获取表格横纵的线
        Mat horizontal = thresh.clone();
        Mat vertical = thresh.clone();
        // 这个值越大，检测到的直线越多
        String parameter = "100";
        if (parameter == null || parameter.equals("")) {
            parameter = "20";
        }
        int scale = Integer.parseInt(parameter); // play with this variable in
        // order to
        // increase/decrease the
        // amount of lines to be
        // detected
        // 使用这个变量来增加/减少待检测的行数

        // Specify size on horizontal axis 指定水平轴上的大小
        int horizontalsize = horizontal.cols() / scale;
        // Create structure element for extracting horizontal lines through
        // morphology operations 创建通过形态学运算提取水平线的结构元素
        // 为了获取横向的表格线，设置腐蚀和膨胀的操作区域为一个比较大的横向直条
        Mat horizontalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(horizontalsize, 1));
        // Apply morphology operations
        // 先腐蚀再膨胀
        // iterations 最后一个参数，迭代次数，越多，线越多。在页面清晰的情况下1次即可。
        Imgproc.erode(horizontal, horizontal, horizontalStructure, new Point(-1, -1), 1);
        Imgproc.dilate(horizontal, horizontal, horizontalStructure, new Point(-1, -1), 1);
        // dilate(horizontal, horizontal, horizontalStructure, Point(-1, -1));
        // // expand horizontal lines

        // Specify size on vertical axis 同上
        int verticalsize = vertical.rows() / scale;
        // Create structure element for extracting vertical lines through
        // morphology operations
        Mat verticalStructure = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, verticalsize));
        Imgproc.erode(vertical, vertical, verticalStructure, new Point(-1, -1), 1);
        Imgproc.dilate(vertical, vertical, verticalStructure, new Point(-1, -1), 1);
		/*
		 * 合并线条 将垂直线，水平线合并为一张图
		 */
        Mat mask = new Mat();
        Core.add(horizontal, vertical, mask);
		/*
		 * 通过 bitwise_and 定位横线、垂直线交汇的点
		 */
        Mat joints = new Mat();
        Core.bitwise_and(horizontal, vertical, joints);
		/*
		 * 通过 findContours 找轮廓
		 *
		 * 第一个参数，是输入图像，图像的格式是8位单通道的图像，并且被解析为二值图像（即图中的所有非零像素之间都是相等的）。 第二个参数，是一个
		 * MatOfPoint 数组，在多数实际的操作中即是STL vectors的STL
		 * vector，这里将使用找到的轮廓的列表进行填充（即，这将是一个contours的vector,其中contours[i]
		 * 表示一个特定的轮廓，这样，contours[i][j]将表示contour[i]的一个特定的端点）。
		 * 第三个参数，hierarchy，这个参数可以指定，也可以不指定。如果指定的话，输出hierarchy，将会描述输出轮廓树的结构信息。
		 * 0号元素表示下一个轮廓（同一层级）；1号元素表示前一个轮廓（同一层级）；2号元素表示第一个子轮廓（下一层级）；3号元素表示父轮廓（
		 * 上一层级） 第四个参数，轮廓的模式，将会告诉OpenCV你想用何种方式来对轮廓进行提取，有四个可选的值： CV_RETR_EXTERNAL
		 * （0）：表示只提取最外面的轮廓； CV_RETR_LIST （1）：表示提取所有轮廓并将其放入列表； CV_RETR_CCOMP
		 * （2）:表示提取所有轮廓并将组织成一个两层结构，其中顶层轮廓是外部轮廓，第二层轮廓是“洞”的轮廓； CV_RETR_TREE
		 * （3）：表示提取所有轮廓并组织成轮廓嵌套的完整层级结构。 第五个参数，见识方法，即轮廓如何呈现的方法，有三种可选的方法：
		 * CV_CHAIN_APPROX_NONE （1）：将轮廓中的所有点的编码转换成点； CV_CHAIN_APPROX_SIMPLE
		 * （2）：压缩水平、垂直和对角直线段，仅保留它们的端点； CV_CHAIN_APPROX_TC89_L1 （3）or
		 * CV_CHAIN_APPROX_TC89_KCOS（4）：应用Teh-Chin链近似算法中的一种风格
		 * 第六个参数，偏移，可选，如果是定，那么返回的轮廓中的所有点均作指定量的偏移
		 */
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE,
                new Point(0, 0));

        List<MatOfPoint> contours_poly = contours;
        Rect[] boundRect = new Rect[contours.size()];
        List<Mat> tables = new ArrayList<Mat>();
        // my
        List<Rect> haveReacts = new ArrayList();
        Map<String, Map<String, Map<String, Double>>> mappoint = new HashMap<String, Map<String, Map<String, Double>>>();
        // 循环所有找到的轮廓-点
        for (int i = 0; i < contours.size(); i++) {
            // 每个表的点
            MatOfPoint point = contours.get(i);
            MatOfPoint contours_poly_point = contours_poly.get(i);
			/*
			 * 获取区域的面积 第一个参数，InputArray contour：输入的点，一般是图像的轮廓点 第二个参数，bool
			 * oriented = false:表示某一个方向上轮廓的的面积值，顺时针或者逆时针，一般选择默认false
			 */
            double area = Imgproc.contourArea(contours.get(i));
            // 如果小于某个值就忽略，代表是杂线不是表格
            if (area < 100) {
                continue;
            }
			/*
			 * approxPolyDP 函数用来逼近区域成为一个形状，true值表示产生的区域为闭合区域。比如一个带点幅度的曲线，变成折线
			 *
			 * MatOfPoint2f curve：像素点的数组数据。 MatOfPoint2f
			 * approxCurve：输出像素点转换后数组数据。 double epsilon：判断点到相对应的line segment
			 * 的距离的阈值。（距离大于此阈值则舍弃，小于此阈值则保留，epsilon越小，折线的形状越“接近”曲线。） bool
			 * closed：曲线是否闭合的标志位。
			 */
            Imgproc.approxPolyDP(new MatOfPoint2f(point.toArray()), new MatOfPoint2f(contours_poly_point.toArray()), 3,
                    true);
            // 为将这片区域转化为矩形，此矩形包含输入的形状
            boundRect[i] = Imgproc.boundingRect(contours_poly.get(i));
            // 找到交汇处的的表区域对象
            Mat table_image = joints.submat(boundRect[i]);

            List<MatOfPoint> table_contours = new ArrayList<MatOfPoint>();
            Mat joint_mat = new Mat();
            Imgproc.findContours(table_image, table_contours, joint_mat, Imgproc.RETR_CCOMP,
                    Imgproc.CHAIN_APPROX_SIMPLE);
            // 从表格的特性看，如果这片区域的点数小于4，那就代表没有一个完整的表格，忽略掉
            if (table_contours.size() < 4) {
                continue;
            }

            // 表格里面的每个点
            Map<String, Double> x_zhis = new HashMap<String, Double>();
            Map<String, Double> y_zhis = new HashMap<String, Double>();
            for (MatOfPoint matOfPoint : table_contours) {
                Point[] array = matOfPoint.toArray();
                for (Point point2 : array) {
                    x_zhis.put("x" + point2.x, point2.x);
                    y_zhis.put("y" + point2.y, point2.y);
                }
            }
            // System.out.println(
            // boundRect[i].x+"|"+boundRect[i].y+"|"+boundRect[i].width+"|"+boundRect[i].height+"|"+table_contours.size()+">>>>>>>>>>>>>>>>>>>");
            // my add
            haveReacts.add(boundRect[i]);
            Map<String, Map<String, Double>> x = new HashMap<String, Map<String, Double>>();
            x.put("x", x_zhis);
            x.put("y", y_zhis);
            mappoint.put("key" + (haveReacts.size() - 1), x);

            // 保存图片
            tables.add(src.submat(boundRect[i]).clone());
            // 将矩形画在原图上
            Imgproc.rectangle(src, boundRect[i].tl(), boundRect[i].br(), new Scalar(255, 0, 255), 1, 8, 0);

        }

        // 页面数据
        Map<String, String> jspdata = new HashMap<String, String>();

        for (int i = 0; i < tables.size(); i++) {
            Mat table = tables.get(i);
            Rect rect = haveReacts.get(i);
            int width = rect.width, height = rect.height;
            Map<String, Map<String, Double>> mapdata = mappoint.get("key" + i);
            int[] x_z = maptoint(mapdata.get("x"));
            int[] y_z = maptoint(mapdata.get("y"));

            // 纵切
            String px_biao = "";
            if (px_biao == null || px_biao.equals("")) {
                px_biao = "5";
            }
            int x_len = 0, x_biao = Integer.parseInt(px_biao);
            List<Mat> mats = new ArrayList<Mat>();
            for (int j = 0; j < x_z.length; j++) {
                if (j == 0) {
                    Mat img = new Mat(table, new Rect(0, 0, x_z[j], height));
                    if (img.cols() > x_biao) {
                        mats.add(img);
                        x_len++;
                    }
                } else {
                    Mat img = new Mat(table, new Rect(x_z[j - 1], 0, x_z[j] - x_z[j - 1], height));
                    if (img.cols() > x_biao) {
                        mats.add(img);
                        x_len++;
                    }
                    if (j == x_z.length - 1) {// 最后一个处理
                        Mat img1 = new Mat(table,
                                new Rect(x_z[x_z.length - 1], 0, width - x_z[x_z.length - 1], height));
                        if (img.cols() > x_biao) {
                            mats.add(img1);
                        }
                    }
                }
            }
            /*imshow(fileTemp, table, "table_" + i + ".png");// 当前table图*/
            // 横切保存 TODO
            String py_biao = "";
            if (py_biao == null || py_biao.equals("")) {
                py_biao = "5";
            }
            int y_len = 0, y_biao = Integer.parseInt(py_biao);
            for (int j = 0; j < mats.size(); j++) {
                Mat mat = mats.get(j);
                int tuwidth = mat.cols(), tugao = mat.rows();
                int cy_len = 0;
                for (int k = 0; k < y_z.length; k++) {
                    if (k == 0) {
                        Mat img = new Mat(mat, new Rect(0, 0, tuwidth, y_z[k]));
                        if (img.rows() > y_biao) {
                            imshow(fileTemp, img, "table_" + i + "_" + j + "_" + cy_len + ".jpg");
                            cy_len++;
                        }
                    } else {
                        Mat img = new Mat(mat, new Rect(0, y_z[k - 1], tuwidth, y_z[k] - y_z[k - 1]));
                        if (img.rows() > y_biao) {
                            imshow(fileTemp, img, "table_" + i + "_" + j + "_" + cy_len + ".jpg");
                            cy_len++;
                        }
                        if (k == y_z.length - 1) {// 最后一个处理
                            Mat img1 = new Mat(mat, new Rect(0, y_z[k], tuwidth, tugao - y_z[k]));
                            if (img.rows() > y_biao) {
                                imshow(fileTemp, img1, "table_" + i + "_" + j + "_" + (cy_len) + ".jpg");
                            }
                        }
                    }
                }
                y_len = cy_len;
            }
            // 保存数据信息
            jspdata.put("table_" + i, x_len + "_" + y_len);
        }
    }

    /**
     * 子图片存储
     * @param fileTemp
     * @param dst
     * @param name
     */
    public void imshow(String fileTemp, Mat dst, String name) {

        Imgcodecs.imwrite(fileTemp + name, dst);
    }

    /**
     * 值计算
     * @param x
     * @return
     */
    public int[] maptoint(Map<String, Double> x) {
        int[] vaule = new int[x.size()];
        int num = 0;
        for (Map.Entry<String, Double> m : x.entrySet()) {
            vaule[num] = m.getValue().intValue();
            num++;
        }
        Arrays.sort(vaule);
        return vaule;
    }

    public void binaryzation(String basePath,String fileName) {
        // 这个必须要写,不写报java.lang.UnsatisfiedLinkError
        /*System.loadLibrary(Core.NATIVE_LIBRARY_NAME);*/
        File imgFile = new File(basePath+fileName);
        //先经过一步灰度化
        Mat src = Imgcodecs.imread(imgFile.toString());
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        src = gray;
        //二值化
        binaryzation(src);
        Imgcodecs.imwrite(basePath+ imgFile.getName(), src);
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
}
