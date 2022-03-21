package com.gmail.wwon.seokk.imagestorage.data.api

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.gmail.wwon.seokk.imagestorage.data.DataResult
import com.gmail.wwon.seokk.imagestorage.data.api.models.ReqThumbnail
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepository
import com.gmail.wwon.seokk.imagestorage.data.database.LocalRepositoryImpl
import com.gmail.wwon.seokk.imagestorage.data.database.entities.HeaderAndThumbnails
import com.gmail.wwon.seokk.imagestorage.data.database.entities.Thumbnail
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@RequiresApi(Build.VERSION_CODES.M)
class ApiRepositoryImpl constructor(
    private val application: Application,
    private val apiService: ApiService,
    private val localRepository: LocalRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ApiRepository {

    override suspend fun getThumbnails(request: ReqThumbnail, isPage: Boolean): Flow<DataResult<List<Thumbnail>>> = flow {
        try {
            emit(DataResult.Loading(true))
            val thumbnails = mutableListOf<Thumbnail>()
            var data: HeaderAndThumbnails = HeaderAndThumbnails.EMPTY

            //키워드 이전 기록 확인
            localRepository.getThumbnails(request.query).let { dataResult ->
                when(dataResult) {
                    is DataResult.Success -> { data = dataResult.data }
                    is DataResult.Error -> throw Exception(dataResult.ex.message)
                    else -> {}
                }
            }

            //검색 기록 없고, 페이징 스크롤 일때 실행
            if((data.header.iPage == 0 && data.header.vPage == 0) || isPage) {
                //이미지 검색 마지막 X & 마지막 페이지 50이하 일 때
                if(!data.header.iIsEnd && data.header.iPage < 50) {
                    request.page = data.header.iPage + 1
                    apiService.getImages(request.query, request.sort, request.page, request.size)?.let { imageList ->
                        if(imageList.meta.totalCount > 0) {
                            //Header 업데이트
                            localRepository.saveHeader(request, imageList.meta, LocalRepositoryImpl.IMAGE)
                            imageList.images.forEach { image ->
                                thumbnails.add(Thumbnail(image.thumbnailUrl, request.query, image.datetime))
                            }
                        }
                    }
                }
                //동영상 검색 마지막 X & 마지막 페이지 15이하 일 때
                if(!data.header.vIsEnd && data.header.iPage < 15) {
                    request.page = data.header.vPage + 1
                    apiService.getVclips(request.query, request.sort, request.page, request.size)?.let { vclipList ->
                        if(vclipList.meta.totalCount > 0) {
                            //Header 업데이트
                            localRepository.saveHeader(request, vclipList.meta, LocalRepositoryImpl.VCLIP)
                            vclipList.vclips.forEach { vclip ->
                                thumbnails.add(Thumbnail(vclip.thumbnail, request.query, vclip.datetime))
                            }
                        }
                    }
                }
                //thumbnail 결과 save
                if(thumbnails.size > 0)
                    localRepository.saveThumbnails(thumbnails)
            }

            //이전 기록 thumbnail add 후 최신 순으로 나열
            thumbnails.addAll(data.thumbnails)
            thumbnails.sortByDescending { s -> s.date }

            emit(DataResult.Success(thumbnails))

        } catch (e: Exception) {
            emit(DataResult.Error(e))
        } finally {
            emit(DataResult.Loading(false))
        }
    }.flowOn(ioDispatcher)

}