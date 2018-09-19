package com.mr.data.common.util.cutImage;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageUtils {
	private static final int BLACK = 0;
	private static final int WHITE = 255;

	private Mat mat;

	/**
	 * 空参构造函数
	 */
	public ImageUtils() {

	}

	/**
	 * 通过图像路径创建一个mat矩阵
	 *
	 * @param imgFilePath
	 *            图像路径
	 */
	public ImageUtils(String imgFilePath) {
		mat = Imgcodecs.imread(imgFilePath);
	}

	public void ImageUtils(Mat mat) {
		this.mat = mat;
	}

	/**
	 * 加载图片
	 *
	 * @param imgFilePath
	 */
	public void loadImg(String imgFilePath) {
		mat = Imgcodecs.imread(imgFilePath);
	}

	/**
	 * 获取图片高度的函数
	 *
	 * @return
	 */
	public int getHeight() {
		return mat.rows();
	}

	/**
	 * 获取图片宽度的函数
	 *
	 * @return
	 */
	public int getWidth() {
		return mat.cols();
	}

	/**
	 * 获取图片像素点的函数
	 *
	 * @param y
	 * @param x
	 * @return
	 */
	public int getPixel(int y, int x) {
		// 我们处理的是单通道灰度图
		return (int) mat.get(y, x)[0];
	}

	/**
	 * 设置图片像素点的函数
	 *
	 * @param y
	 * @param x
	 * @param color
	 */
	public void setPixel(int y, int x, int color) {
		// 我们处理的是单通道灰度图
		mat.put(y, x, color);
	}

	/**
	 * 保存图片的函数
	 *
	 * @param filename
	 * @return
	 */
	public boolean saveImg(String filename) {
		return Imgcodecs.imwrite(filename, mat);
	}
	public void toGray(String filename) {
		// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		File imgFile = new File(filename);

		//方式一
		Mat src = Imgcodecs.imread(imgFile.toString(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		//保存灰度化的图片
		Imgcodecs.imwrite(imgFile.getName(), src);
	}
	public void toGray2(String filename) {
		// 这个必须要写,不写报java.lang.UnsatisfiedLinkError
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		File imgFile = new File(filename);

		//方式二
		Mat src = Imgcodecs.imread(imgFile.toString());
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		src = gray;
		//保存灰度化的图片
		Imgcodecs.imwrite(imgFile.getName(), src);
	}

	/**
	 * 8邻域降噪,又有点像9宫格降噪;即如果9宫格中心被异色包围，则同化
	 * @param pNum 默认值为1
	 */
	public void navieRemoveNoise(int pNum) {
		int i, j, m, n, nValue, nCount;
		int nWidth = getWidth(), nHeight = getHeight();

		// 对图像的边缘进行预处理
		for (i = 0; i < nWidth; ++i) {
			setPixel(i, 0, WHITE);
			setPixel(i, nHeight - 1, WHITE);
		}

		for (i = 0; i < nHeight; ++i) {
			setPixel(0, i, WHITE);
			setPixel(nWidth - 1, i, WHITE);
		}

		// 如果一个点的周围都是白色的，而它确是黑色的，删除它
		for (j = 1; j < nHeight - 1; ++j) {
			for (i = 1; i < nWidth - 1; ++i) {
				nValue = getPixel(j, i);
				if (nValue == 0) {
					nCount = 0;
					// 比较以(j ,i)为中心的9宫格，如果周围都是白色的，同化
					for (m = j - 1; m <= j + 1; ++m) {
						for (n = i - 1; n <= i + 1; ++n) {
							if (getPixel(m, n) == 0) {
								nCount++;
							}
						}
					}
					if (nCount <= pNum) {
						// 周围黑色点的个数小于阀值pNum,把该点设置白色
						setPixel(j, i, WHITE);
					}
				} else {
					nCount = 0;
					// 比较以(j ,i)为中心的9宫格，如果周围都是黑色的，同化
					for (m = j - 1; m <= j + 1; ++m) {
						for (n = i - 1; n <= i + 1; ++n) {
							if (getPixel(m, n) == 0) {
								nCount++;
							}
						}
					}
					if (nCount >= 7) {
						// 周围黑色点的个数大于等于7,把该点设置黑色;即周围都是黑色
						setPixel(j, i, BLACK);
					}
				}
			}
		}

	}

	/**
	 * 连通域降噪
	 * @param pArea 默认值为1
	 */
	public void contoursRemoveNoise(double pArea) {
		int i, j, color = 1;
		int nWidth = getWidth(), nHeight = getHeight();

		for (i = 0; i < nWidth; ++i) {
			for (j = 0; j < nHeight; ++j) {
				if (getPixel(j, i) == BLACK) {
					//用不同颜色填充连接区域中的每个黑色点
					//floodFill就是把一个点x的所有相邻的点都涂上x点的颜色，一直填充下去，直到这个区域内所有的点都被填充完为止
					Imgproc.floodFill(mat, new Mat(), new Point(i, j), new Scalar(color));
					color++;
				}
			}
		}

		//统计不同颜色点的个数
		int[] ColorCount = new int[255];

		for (i = 0; i < nWidth; ++i) {
			for (j = 0; j < nHeight; ++j) {
				if (getPixel(j, i) != 255) {
					ColorCount[getPixel(j, i) - 1]++;
				}
			}
		}

		//去除噪点
		for (i = 0; i < nWidth; ++i) {
			for (j = 0; j < nHeight; ++j) {

				if (ColorCount[getPixel(j, i) - 1] <= pArea) {
					setPixel(j, i, WHITE);
				}
			}
		}

		for (i = 0; i < nWidth; ++i) {
			for (j = 0; j < nHeight; ++j) {
				if (getPixel(j, i) < WHITE) {
					setPixel(j, i, BLACK);
				}
			}
		}

	}

	// 图像腐蚀/膨胀处理
	public void erodeImg() {
		Mat outImage = new Mat();

		// size 越小，腐蚀的单位越小，图片越接近原图
		Mat structImage = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 2));

		/**
		 * 图像腐蚀
		 * 腐蚀说明： 图像的一部分区域与指定的核进行卷积，
		 * 求核的最`小`值并赋值给指定区域。
		 * 腐蚀可以理解为图像中`高亮区域`的'领域缩小'。
		 * 意思是高亮部分会被不是高亮部分的像素侵蚀掉，使高亮部分越来越少。
		 */
		Imgproc.erode(mat, outImage, structImage, new Point(-1, -1), 2);
		mat = outImage;

		/**
		 * 膨胀
		 * 膨胀说明： 图像的一部分区域与指定的核进行卷积，
		 * 求核的最`大`值并赋值给指定区域。
		 * 膨胀可以理解为图像中`高亮区域`的'领域扩大'。
		 * 意思是高亮部分会侵蚀不是高亮的部分，使高亮部分越来越多。
		 */
		Imgproc.dilate(mat, outImage, structImage , new Point(-1, -1), 2);
		mat = outImage;

	}

	// 图像切割,水平投影法切割
	public List<Mat> cutImgX() {
		int i, j;
		int nWidth = getWidth(), nHeight = getHeight();
		int[] xNum = new int[nHeight], cNum;
		int average = 0;// 记录像素的平均值
		// 统计出每行黑色像素点的个数
		for (i = 0; i < nHeight; i++) {
			for (j = 0; j < nWidth; j++) {
				if (getPixel(i, j) == BLACK) {
					xNum[i]++;
				}

			}
		}

		// 经过测试这样得到的平均值最优
		cNum = Arrays.copyOf(xNum, xNum.length);
		Arrays.sort(cNum);
		for (i = 31 * nHeight / 32; i < nHeight; i++) {
			average += cNum[i];
		}
		average /= (nHeight / 32);

		// 把需要切割的y点都存到cutY中
		List<Integer> cutY = new ArrayList<Integer>();
		for (i = 0; i < nHeight; i++) {
			if (xNum[i] > average) {
				cutY.add(i);
			}
		}

		// 优化cutY把
		if (cutY.size() != 0) {

			int temp = cutY.get(cutY.size() - 1);
			// 因为线条有粗细,优化cutY
			for (i = cutY.size() - 2; i >= 0; i--) {
				int k = temp - cutY.get(i);
				if (k <= 8) {
					cutY.remove(i);
				} else {
					temp = cutY.get(i);

				}

			}
		}
		System.out.println("===================XX============:"+cutY.size());
		// 把切割的图片都保存到YMat中
		List<Mat> YMat = new ArrayList<Mat>();
		for (i = 1; i < cutY.size(); i++) {
			// 设置感兴趣的区域
			int startY = cutY.get(i - 1);
			int height = cutY.get(i) - startY;
			Mat temp = new Mat(mat, new Rect(0, startY, nWidth, height));
			Mat t = new Mat();
			temp.copyTo(t);
			YMat.add(t);
		}

		return YMat;
	}


	// 图像切割,垂直投影法切割
	public List<Mat> cutImgY() {

		int i, j;
		int nWidth = getWidth(), nHeight = getHeight();
		int[] xNum = new int[nWidth], cNum;
		int average = 0;// 记录像素的平均值
		// 统计出每列黑色像素点的个数
		for (i = 0; i < nWidth; i++) {
			for (j = 0; j < nHeight; j++) {
				if (getPixel(j, i) == BLACK) {
					xNum[i]++;
				}

			}
		}

		// 经过测试这样得到的平均值最优 , 平均值的选取很重要
		cNum = Arrays.copyOf(xNum, xNum.length);
		Arrays.sort(cNum);
		for (i = 31 * nWidth / 32; i < nWidth; i++) {
			average += cNum[i];
		}
		average /= (nWidth / 28);

		// 把需要切割的x点都存到cutY中,
		List<Integer> cutX = new ArrayList<Integer>();
		for (i = 0; i < nWidth; i += 2) {
			if (xNum[i] >= average) {
				cutX.add(i);
			}
		}

		if (cutX.size() != 0) {

			int temp = cutX.get(cutX.size() - 1);
			// 因为线条有粗细,优化cutY
			for (i = cutX.size() - 2; i >= 0; i--) {
				int k = temp - cutX.get(i);
				if (k <= 10) {
					cutX.remove(i);
				} else {
					temp = cutX.get(i);

				}

			}
		}
		System.out.println("===================YY============:"+cutX.size());
		// 把切割的图片都保存到YMat中
		List<Mat> XMat = new ArrayList<Mat>();
		for (i = 1; i < cutX.size(); i++) {
			// 设置感兴趣的区域
			int startX = cutX.get(i - 1);
			int width = cutX.get(i) - startX;
			Mat temp = new Mat(mat, new Rect(startX, 0, width, nHeight));
			Mat t = new Mat();
			temp.copyTo(t);
			XMat.add(t);
		}

		return XMat;
	}




	/**
	 * 二值化图片
	 * @param mat
	 */
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


	/**
	 * 二值化图片
	 * @param
	 */
	public void binaryzation() {
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
	}
}
